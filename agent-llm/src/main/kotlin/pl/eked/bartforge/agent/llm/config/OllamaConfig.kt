package pl.eked.bartforge.agent.llm.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(OllamaProperties::class)
class OllamaConfig {

    @Bean
    fun ollamaWebClient(properties: OllamaProperties): WebClient {
        return WebClient.builder()
            .baseUrl(properties.baseUrl)
            .build()
    }
}