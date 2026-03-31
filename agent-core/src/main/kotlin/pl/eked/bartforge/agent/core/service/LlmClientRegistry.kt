package pl.eked.pl.eked.bartforge.agent.core.service

import pl.eked.pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.pl.eked.bartforge.agent.core.port.LlmClient

class LlmClientRegistry(
    clients: List<LlmClient>,
) {
    private val clientsByProvider: Map<LlmProvider, LlmClient> = clients.associateBy { it.provider() }

    fun get(provider: LlmProvider): LlmClient {
        return clientsByProvider[provider]
            ?: error("Missing LLM client for provider: $provider")
    }
}