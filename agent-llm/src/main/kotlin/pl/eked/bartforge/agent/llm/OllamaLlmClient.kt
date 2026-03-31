package pl.eked.bartforge.agent.llm

import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.port.LlmClient

@Component
class OllamaLlmClient : LlmClient {
    override fun provider(): LlmProvider = LlmProvider.OLLAMA

    override fun chat(prompt: String): String {
        return "OLLAMA response for prompt: $prompt"
    }
}