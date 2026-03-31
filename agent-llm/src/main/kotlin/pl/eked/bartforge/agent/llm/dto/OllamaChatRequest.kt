package pl.eked.bartforge.agent.llm.dto

data class OllamaChatRequest(
    val model: String,
    val messages: List<OllamaMessage>,
    val stream: Boolean = false
)
