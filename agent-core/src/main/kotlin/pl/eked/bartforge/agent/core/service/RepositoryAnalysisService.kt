package pl.eked.bartforge.agent.core.service

import pl.eked.bartforge.agent.core.model.RepositoryAnalysisRequest
import pl.eked.bartforge.agent.core.model.RepositoryAnalysisResponse

class RepositoryAnalysisService(
    private val llmClientRegistry: LlmClientRegistry,
    private val repositoryContextService: RepositoryContextService
) {

    fun analyze(request: RepositoryAnalysisRequest): RepositoryAnalysisResponse {
        val repositoryContext = repositoryContextService.buildRepositoryContext(request.repositoryPath)
        val llmClient = llmClientRegistry.get(request.provider)

        val finalPrompt = buildPrompt(
            userPrompt = request.prompt,
            repositoryPath = request.repositoryPath,
            context = repositoryContext
        )

        val answer = llmClient.chat(finalPrompt)

        return RepositoryAnalysisResponse(
            provider = request.provider,
            repositoryPath = request.repositoryPath,
            analyzedFiles = repositoryContext.fileContents.map { it.path },
            answer = answer
        )
    }

    private fun buildPrompt(
        userPrompt: String,
        repositoryPath: String,
        context: RepositoryContext
    ): String {
        val filesSection = context.files.joinToString(separator = "\n") { "- $it" }

        val contentSection = context.fileContents.joinToString(separator = "\n\n---\n\n") { file ->
            """
            FILE: ${file.path}
            ${file.content}
            """.trimIndent()
        }

        return """
            You are analyzing a local coding-agent project.

            Repository path:
            $repositoryPath

            User request:
            $userPrompt

            Visible repository files:
            $filesSection

            File contents:
            $contentSection

            Please answer in the same language that you were requested.
            Focus on architecture, important observations, and practical next steps.
        """.trimIndent()
    }
}