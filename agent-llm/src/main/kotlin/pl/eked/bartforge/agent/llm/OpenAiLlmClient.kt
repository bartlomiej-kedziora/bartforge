package pl.eked.bartforge.agent.llm

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.port.LlmClient
import pl.eked.bartforge.agent.llm.config.OpenAiProperties
import pl.eked.bartforge.agent.llm.dto.OpenAiChatRequest
import pl.eked.bartforge.agent.llm.dto.OpenAiChatResponse
import pl.eked.bartforge.agent.llm.exception.OpenAiException
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.TimeoutException

@Component
class OpenAiLlmClient(
    private val openAiWebClient: WebClient,
    private val openAiProperties: OpenAiProperties
) : LlmClient {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(OpenAiLlmClient::class.java)
    }
    override fun provider(): LlmProvider = LlmProvider.OPENAI

    override fun chat(prompt: String): String {
        val request = OpenAiChatRequest(
            model = openAiProperties.model,
            input = prompt
        )

        val startTime = System.currentTimeMillis()

        log.info(
            "Sending chat request to OpenAI. model={}, promptLength={}", openAiProperties.model,
            prompt.length
        )

        val response = this.callOpenAi(request)

        log.info(
            "Received response from OpenAI. model={}, durationMs={}, responseLength={}",
            openAiProperties.model,
            System.currentTimeMillis() - startTime,
            response.length
        )

        return response
    }

    private fun callOpenAi(request: OpenAiChatRequest): String {
        return runCatching {
            val response = openAiWebClient.post()
                .uri("/responses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError) { clientResponse ->
                    clientResponse.bodyToMono(String::class.java)
                        .defaultIfEmpty("")
                        .flatMap { responseBody ->
                            Mono.error(
                                OpenAiException(
                                    "OpenAI returned HTTP ${clientResponse.statusCode().value()}: $responseBody"
                                )
                            )
                        }
                }
                .bodyToMono(OpenAiChatResponse::class.java)
                .timeout(Duration.ofSeconds(openAiProperties.timeoutSeconds))
                .block()
                ?: throw OpenAiException("Empty response from OpenAI")

            return@runCatching response.output
                .asSequence()
                .filter { it.type == "message" }
                .flatMap { it.content.asSequence() }
                .firstOrNull { it.type == "output_text" }
                ?.text
        }.onFailure {
            when (it) {
                is OpenAiException -> throw it
                is WebClientResponseException -> throw OpenAiException(
                    "HTTP error while calling OpenAI: ${it.statusCode.value()} ${it.responseBodyAsString}", it)
                is TimeoutException -> throw OpenAiException(
                    "Timeout while calling OpenAI after ${openAiProperties.timeoutSeconds} seconds", it)
                else -> throw OpenAiException("Unexpected error while calling OpenAI: ${it.message}", it)
            }
        }.getOrNull()?.takeIf { it.isNotBlank() }
            ?: throw OpenAiException("OpenAI response does not contain output_text")
    }
}