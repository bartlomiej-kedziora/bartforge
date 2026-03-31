package pl.eked.bartforge.agent.api.dto

data class RepositoryAnalysisResponseDto(
    val provider: String,
    val repositoryPath: String,
    val analyzedFiles: List<String>,
    val answer: String
)