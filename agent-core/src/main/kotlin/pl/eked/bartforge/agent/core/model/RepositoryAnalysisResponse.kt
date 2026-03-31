package pl.eked.bartforge.agent.core.model

data class RepositoryAnalysisResponse(
    val provider: LlmProvider,
    val repositoryPath: String,
    val analyzedFiles: List<String>,
    val answer: String
)