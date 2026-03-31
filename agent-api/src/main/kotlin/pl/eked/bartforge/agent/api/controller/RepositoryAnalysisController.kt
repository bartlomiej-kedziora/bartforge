package pl.eked.bartforge.agent.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.eked.bartforge.agent.api.dto.RepositoryAnalysisRequestDto
import pl.eked.bartforge.agent.api.dto.RepositoryAnalysisResponseDto
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.model.RepositoryAnalysisRequest
import pl.eked.bartforge.agent.core.service.RepositoryAnalysisService

@RestController
@RequestMapping("/api/agent")
class RepositoryAnalysisController(
    private val repositoryAnalysisService: RepositoryAnalysisService
) {

    @PostMapping("/analyze-repository")
    fun analyzeRepository(
        @RequestBody request: RepositoryAnalysisRequestDto
    ): RepositoryAnalysisResponseDto {
        val response = repositoryAnalysisService.analyze(
            RepositoryAnalysisRequest(
                prompt = request.prompt,
                provider = LlmProvider.valueOf(request.provider.uppercase()),
                repositoryPath = request.repositoryPath
            )
        )

        return RepositoryAnalysisResponseDto(
            provider = response.provider.name,
            repositoryPath = response.repositoryPath,
            analyzedFiles = response.analyzedFiles,
            answer = response.answer
        )
    }
}