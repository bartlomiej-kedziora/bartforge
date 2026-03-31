package pl.eked.bartforge.agent.llm.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiContentItem(
    val type: String? = null,
    val text: String? = null
)