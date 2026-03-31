package pl.eked.bartforge.agent.llm.dto

data class OpenAiChatResponse(
    val output: List<OpenAiOutputItem> = emptyList()
)