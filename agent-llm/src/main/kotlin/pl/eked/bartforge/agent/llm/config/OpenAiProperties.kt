package pl.eked.bartforge.agent.llm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bartforge.llm.openai")
data class OpenAiProperties(
    val baseUrl: String,
    val apiKey: String,
    val model: String,
    val timeoutSeconds: Long = 30
)