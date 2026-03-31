package pl.eked.bartforge.agent.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.eked.pl.eked.bartforge.agent.core.port.LlmClient
import pl.eked.pl.eked.bartforge.agent.core.service.AgentService
import pl.eked.pl.eked.bartforge.agent.core.service.LlmClientRegistry

@Configuration
class CoreConfig {

    @Bean
    fun llmClientRegistry(clients: List<LlmClient>): LlmClientRegistry {
        return LlmClientRegistry(clients)
    }

    @Bean
    fun agentService(
        llmClientRegistry: LlmClientRegistry
    ): AgentService {
        return AgentService(llmClientRegistry)
    }
}