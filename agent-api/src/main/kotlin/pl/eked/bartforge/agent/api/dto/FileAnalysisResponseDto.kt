package pl.eked.bartforge.agent.api.dto

data class FileAnalysisResponseDto(
    val provider: String,
    val filePath: String,
    val answer: String
)