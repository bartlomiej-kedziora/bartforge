package pl.eked.bartforge.agent.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.eked.bartforge.agent.api.dto.FileAnalysisRequestDto
import pl.eked.bartforge.agent.api.dto.FileAnalysisResponseDto
import pl.eked.bartforge.agent.core.model.FileAnalysisRequest
import pl.eked.bartforge.agent.core.model.LlmProvider
import pl.eked.bartforge.agent.core.service.FileAnalysisService

@RestController
@RequestMapping("/api/agent")
class FileAnalysisController(
    private val fileAnalysisService: FileAnalysisService
) {

    @PostMapping("/analyze-file")
    fun analyzeFile(
        @RequestBody request: FileAnalysisRequestDto
    ): FileAnalysisResponseDto {
        val response = fileAnalysisService.analyze(
            FileAnalysisRequest(
                prompt = request.prompt,
                provider = LlmProvider.valueOf(request.provider.uppercase()),
                filePath = request.filePath
            )
        )

        return FileAnalysisResponseDto(
            provider = response.provider.name,
            filePath = response.filePath,
            answer = response.answer
        )
    }
}