package pl.eked.bartforge.agent.api.dto

data class RepositoryAnalysisRequestDto(
    val prompt: String,
    val provider: String,
    val repositoryPath: String
)