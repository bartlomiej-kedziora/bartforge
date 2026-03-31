package pl.eked.bartforge.agent.core.model

data class RepositoryAnalysisRequest(
    val prompt: String,
    val provider: LlmProvider,
    val repositoryPath: String
)