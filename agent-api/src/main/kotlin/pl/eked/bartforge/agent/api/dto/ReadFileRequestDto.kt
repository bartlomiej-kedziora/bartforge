package pl.eked.bartforge.agent.api.dto

data class ReadFileRequestDto(
    val path: String,
    val fromLine: Int = 1,
    val toLine: Int? = null,
    val maxCharacters: Int = 20_000
)