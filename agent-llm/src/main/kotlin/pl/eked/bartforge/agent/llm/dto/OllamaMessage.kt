package pl.eked.bartforge.agent.llm.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OllamaMessage(
    val role: String,
    val content: String
)
