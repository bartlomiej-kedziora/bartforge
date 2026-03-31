package pl.eked.bartforge.agent.core.model

data class FileAnalysisRequest(
    val prompt: String,
    val provider: LlmProvider,
    val filePath: String
)