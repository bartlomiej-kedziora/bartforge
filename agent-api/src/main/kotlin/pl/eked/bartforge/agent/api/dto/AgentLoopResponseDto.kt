package pl.eked.bartforge.agent.api.dto

data class AgentLoopResponseDto(
    val provider: String,
    val answer: String,
    val usedTools: List<String>
)