package pl.eked.bartforge.agent.api.controller

import pl.eked.bartforge.agent.api.dto.AgentChatRequestDto
import pl.eked.bartforge.agent.api.dto.AgentChatResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.eked.bartforge.agent.core.model.AgentRequest
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.service.AgentService

@RestController
@RequestMapping("/api/agent")
class AgentController(
    private val agentService: AgentService
) {

    @PostMapping("/chat")
    fun chat(@RequestBody request: AgentChatRequestDto): AgentChatResponseDto {
        val response = agentService.execute(
            AgentRequest(
                prompt = request.prompt,
                provider = LlmProvider.valueOf(request.provider.uppercase()),
                repositoryPath = request.repositoryPath
            )
        )

        return AgentChatResponseDto(
            provider = response.provider.name,
            answer = response.answer
        )
    }
}