package pl.eked.bartforge.agent.api.dto

data class AgentLoopRequestDto(
    val prompt: String,
    val provider: String
)