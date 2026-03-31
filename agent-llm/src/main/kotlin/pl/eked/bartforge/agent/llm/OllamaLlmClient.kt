package pl.eked.bartforge.agent.llm

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.port.LlmClient
import pl.eked.bartforge.agent.llm.config.OllamaProperties
import pl.eked.bartforge.agent.llm.dto.OllamaChatRequest
import pl.eked.bartforge.agent.llm.dto.OllamaChatResponse
import pl.eked.bartforge.agent.llm.dto.OllamaMessage
import pl.eked.bartforge.agent.llm.exception.OllamaException
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.TimeoutException

@Component
class OllamaLlmClient(
    private val ollamaWebClient: WebClient,
    private val ollamaProperties: OllamaProperties
) : LlmClient {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(OllamaLlmClient::class.java)
    }

    override fun provider(): LlmProvider = LlmProvider.OLLAMA

    override fun chat(prompt: String): String {
        val request = OllamaChatRequest(
            model = ollamaProperties.model,
            messages = listOf(
                OllamaMessage(
                    role = "user",
                    content = prompt
                )
            ),
            stream = false
        )

        val startTime = System.currentTimeMillis()

        log.info("Sending chat request to Ollama. model={}, promptLength={}", ollamaProperties.model,
            prompt.length
        )

        val response = this.callOllama(request)

        log.info(
            "Received response from Ollama. model={}, durationMs={}, responseLength={}",
            ollamaProperties.model,
            System.currentTimeMillis() - startTime,
            response.length
        )

        return response
    }

    private fun callOllama(request: OllamaChatRequest): String {
        return runCatching {
            val response = ollamaWebClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError) { clientResponse ->
                    clientResponse.bodyToMono(String::class.java)
                        .defaultIfEmpty("")
                        .flatMap { responseBody ->
                            Mono.error(
                                OllamaException(
                                    "Ollama returned HTTP ${clientResponse.statusCode().value()}: $responseBody"
                                )
                            )
                        }
                }
                .bodyToMono(OllamaChatResponse::class.java)
                .timeout(Duration.ofSeconds(ollamaProperties.timeoutSeconds))
                .block()
                ?: throw OllamaException("Empty response from Ollama")

            return@runCatching response.message?.content
        }.onFailure {
            when (it) {
                is OllamaException -> throw it
                is WebClientResponseException -> throw OllamaException(
                    "HTTP error while calling Ollama: ${it.statusCode.value()} ${it.responseBodyAsString}", it)
                is TimeoutException -> throw OllamaException(
                    "Timeout while calling Ollama after ${ollamaProperties.timeoutSeconds} seconds", it)
                else -> throw OllamaException("Unexpected error while calling Ollama: ${it.message}", it)
            }
        }.getOrNull()?.takeIf { it.isNotBlank() }
            ?: throw OllamaException("Ollama response does not contain message content")
    }
}