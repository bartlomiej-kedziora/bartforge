package pl.eked.bartforge.agent.llm

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.port.LlmClient
import pl.eked.bartforge.agent.llm.config.OllamaProperties
import pl.eked.bartforge.agent.llm.dto.OllamaChatRequest
import pl.eked.bartforge.agent.llm.dto.OllamaChatResponse
import pl.eked.bartforge.agent.llm.dto.OllamaMessage
import java.time.Duration

@Component
class OllamaLlmClient(
    private val ollamaWebClient: WebClient,
    private val ollamaProperties: OllamaProperties
) : LlmClient {

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

        val response = ollamaWebClient.post()
            .uri("/api/chat")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaChatResponse::class.java)
            .block(Duration.ofSeconds(30))
            ?: throw IllegalStateException("Empty response from Ollama")

        return response.message?.content
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Ollama response does not contain message content")
    }
}