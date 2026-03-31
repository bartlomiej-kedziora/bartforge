package pl.eked.bartforge.agent.api.dto

data class AgentChatRequestDto(
    val prompt: String,
    val provider: String,
    val repositoryPath: String? = null
)