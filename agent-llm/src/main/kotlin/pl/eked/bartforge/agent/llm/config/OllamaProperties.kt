package pl.eked.bartforge.agent.llm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bartforge.llm.ollama")
data class OllamaProperties(
    val baseUrl: String,
    val model: String,
    val timeoutSeconds: Long = 30
)
