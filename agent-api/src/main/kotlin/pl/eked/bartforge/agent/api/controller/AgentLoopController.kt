package pl.eked.bartforge.agent.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.eked.bartforge.agent.api.dto.AgentLoopRequestDto
import pl.eked.bartforge.agent.api.dto.AgentLoopResponseDto
import pl.eked.bartforge.agent.core.agent.AgentLoopRequest
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.service.AgentLoopService

@RestController
@RequestMapping("/api/agent")
class AgentLoopController(
    private val agentLoopService: AgentLoopService
) {

    @PostMapping("/loop")
    fun loop(@RequestBody request: AgentLoopRequestDto): AgentLoopResponseDto {
        val response = agentLoopService.execute(
            AgentLoopRequest(
                prompt = request.prompt,
                provider = LlmProvider.valueOf(request.provider.uppercase())
            )
        )

        return AgentLoopResponseDto(
            provider = response.provider.name,
            answer = response.answer,
            usedTools = response.usedTools
        )
    }
}