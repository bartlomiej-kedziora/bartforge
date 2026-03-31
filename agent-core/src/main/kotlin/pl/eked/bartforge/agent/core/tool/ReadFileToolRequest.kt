package pl.eked.bartforge.agent.core.tool

data class ReadFileToolRequest(
    val path: String,
    val fromLine: Int = 1,
    val toLine: Int? = null,
    val maxCharacters: Int = 20_000
)