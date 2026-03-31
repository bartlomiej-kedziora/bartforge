package pl.eked.pl.eked.bartforge.agent.core.service

import pl.eked.pl.eked.bartforge.agent.core.model.AgentRequest
import pl.eked.pl.eked.bartforge.agent.core.model.AgentResponse

class AgentService(
    private val llmClientRegistry: LlmClientRegistry
) {
    fun execute(request: AgentRequest): AgentResponse {
        val client = llmClientRegistry.get(request.provider)
        val answer = client.chat(request.prompt)

        return AgentResponse(
            provider = request.provider,
            answer = answer
        )
    }
}