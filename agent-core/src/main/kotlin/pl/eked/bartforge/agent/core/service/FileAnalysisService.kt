package pl.eked.bartforge.agent.core.service

import pl.eked.bartforge.agent.core.model.FileAnalysisRequest
import pl.eked.bartforge.agent.core.model.FileAnalysisResponse
import pl.eked.bartforge.agent.core.tool.ReadFileTool
import pl.eked.bartforge.agent.core.tool.ReadFileToolRequest

class FileAnalysisService(
    private val llmClientRegistry: LlmClientRegistry,
    private val readFileTool: ReadFileTool
) {

    fun analyze(request: FileAnalysisRequest): FileAnalysisResponse {
        val fileContent = readFileTool.execute(
            ReadFileToolRequest(
                path = request.filePath,
                fromLine = 1,
                toLine = 120,
                maxCharacters = 5_000
            )
        ).content

        val prompt = buildPrompt(
            userPrompt = request.prompt,
            filePath = request.filePath,
            fileContent = fileContent
        )

        val llmClient = llmClientRegistry.get(request.provider)
        val answer = llmClient.chat(prompt)

        return FileAnalysisResponse(
            provider = request.provider,
            filePath = request.filePath,
            answer = answer
        )
    }

    private fun buildPrompt(
        userPrompt: String,
        filePath: String,
        fileContent: String
    ): String {
        return """
            You are analyzing a single file in a local coding-agent project.

            User request:
            $userPrompt

            File path:
            $filePath

            File content:
            $fileContent

            Please answer in the same language that you were requested.
            Focus only on this file.
            Keep the answer practical and concise.
        """.trimIndent()
    }
}