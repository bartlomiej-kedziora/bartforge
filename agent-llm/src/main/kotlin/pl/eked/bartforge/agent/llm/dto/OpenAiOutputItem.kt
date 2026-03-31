package pl.eked.bartforge.agent.llm.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiOutputItem(
    val type: String? = null,
    val content: List<OpenAiContentItem> = emptyList()
)