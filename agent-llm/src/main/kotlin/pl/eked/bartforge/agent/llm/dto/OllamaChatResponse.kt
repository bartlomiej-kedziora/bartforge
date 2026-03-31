package pl.eked.bartforge.agent.llm.dto

data class OllamaChatResponse(
    val model: String? = null,
    val message: OllamaMessage? = null,
    val done: Boolean? = null
)
