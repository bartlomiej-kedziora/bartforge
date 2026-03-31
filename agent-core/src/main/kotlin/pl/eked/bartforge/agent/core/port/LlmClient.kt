package pl.eked.pl.eked.bartforge.agent.core.port

import pl.eked.pl.eked.bartforge.agent.core.model.LlmProvider

interface LlmClient {
    fun provider(): LlmProvider
    fun chat(prompt: String): String
}