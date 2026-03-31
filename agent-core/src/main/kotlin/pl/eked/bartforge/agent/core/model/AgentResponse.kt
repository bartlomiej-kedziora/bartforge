package pl.eked.pl.eked.bartforge.agent.core.model

data class AgentResponse(
    val provider: LlmProvider,
    val answer: String
)
