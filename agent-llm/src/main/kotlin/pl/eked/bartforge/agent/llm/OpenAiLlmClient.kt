package pl.eked.bartforge.agent.llm

import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.port.LlmClient

@Component
class OpenAiLlmClient : LlmClient {
    override fun provider(): LlmProvider = LlmProvider.OPENAI

    override fun chat(prompt: String): String {
        return "OPENAI response for prompt: $prompt"
    }
}