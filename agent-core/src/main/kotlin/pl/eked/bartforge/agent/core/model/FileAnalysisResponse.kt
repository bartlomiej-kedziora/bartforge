package pl.eked.bartforge.agent.core.model

data class FileAnalysisResponse(
    val provider: LlmProvider,
    val filePath: String,
    val answer: String
)