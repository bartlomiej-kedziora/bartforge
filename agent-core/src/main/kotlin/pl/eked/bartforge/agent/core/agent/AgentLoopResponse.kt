package pl.eked.bartforge.agent.core.agent

import pl.eked.bartforge.agent.core.model.LlmProvider

data class AgentLoopResponse(
    val provider: LlmProvider,
    val answer: String,
    val usedTools: List<String>
)