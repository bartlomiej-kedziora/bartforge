package pl.eked.bartforge.agent.llm.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(OpenAiProperties::class)
class OpenAiConfig {

    @Bean
    fun openAiWebClient(properties: OpenAiProperties): WebClient {
        return WebClient.builder()
            .baseUrl(properties.baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ${properties.apiKey}")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
    }
}