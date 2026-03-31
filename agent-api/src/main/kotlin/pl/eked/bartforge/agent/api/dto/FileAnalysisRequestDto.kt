package pl.eked.bartforge.agent.api.dto

data class FileAnalysisRequestDto(
    val prompt: String,
    val provider: String,
    val filePath: String
)