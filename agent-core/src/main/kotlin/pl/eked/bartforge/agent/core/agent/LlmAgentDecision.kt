package pl.eked.bartforge.agent.core.agent

data class LlmAgentDecision(
    val type: String? = null,
    val toolName: String? = null,
    val arguments: Map<String, Any?> = emptyMap(),
    val answer: String? = null
)