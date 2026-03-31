package pl.eked.bartforge.agent.llm

import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.port.LlmClient

@Component
class GeminiLlmClient : LlmClient {
    override fun provider(): LlmProvider = LlmProvider.GEMINI

    override fun chat(prompt: String): String {
        return "GEMINI response for prompt: $prompt"
    }
}