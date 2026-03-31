package pl.eked.bartforge.agent.llm.dto

data class OpenAiChatRequest(
    val model: String,
    val input: String
)