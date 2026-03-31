package pl.eked.bartforge.agent.core.model

data class AgentRequest(
    val prompt: String,
    val provider: LlmProvider,
    val repositoryPath: String? = null
)
