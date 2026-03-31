package pl.eked.bartforge.agent.core.agent

import pl.eked.bartforge.agent.core.model.LlmProvider

data class AgentLoopRequest(
    val prompt: String,
    val provider: LlmProvider
)