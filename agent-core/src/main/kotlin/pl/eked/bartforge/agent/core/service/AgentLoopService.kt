package pl.eked.bartforge.agent.core.service

import pl.eked.bartforge.agent.core.agent.AgentDecisionParser
import pl.eked.bartforge.agent.core.agent.AgentLoopRequest
import pl.eked.bartforge.agent.core.agent.AgentLoopResponse
import pl.eked.bartforge.agent.core.agent.LlmAgentDecision
import pl.eked.bartforge.agent.core.agent.ToolRegistry

class AgentLoopService(
    private val llmClientRegistry: LlmClientRegistry,
    private val toolRegistry: ToolRegistry,
    private val agentDecisionParser: AgentDecisionParser
) {

    companion object {
        private const val MAX_TOOL_RESULT_CHARACTERS = 4_000
    }

    fun execute(request: AgentLoopRequest): AgentLoopResponse {
        val llmClient = llmClientRegistry.get(request.provider)
        val usedTools = mutableListOf<String>()

        val firstPrompt = buildInitialPrompt(request.prompt)
        val firstResponse = llmClient.chat(firstPrompt)
        val firstDecision = normalizeDecision(
            rawResponse = firstResponse,
            parsedDecision = agentDecisionParser.parse(firstResponse)
        )

        return when (firstDecision.type) {
            "final_answer" -> AgentLoopResponse(
                provider = request.provider,
                answer = firstDecision.answer ?: firstResponse,
                usedTools = usedTools
            )

            "tool_call" -> {
                val toolName = firstDecision.toolName
                    ?: return AgentLoopResponse(
                        provider = request.provider,
                        answer = fallbackAnswer(firstResponse),
                        usedTools = usedTools
                    )

                val toolResult = runCatching {
                    toolRegistry.execute(
                        toolName = toolName,
                        arguments = firstDecision.arguments
                    )
                }.getOrElse { exception ->
                    return AgentLoopResponse(
                        provider = request.provider,
                        answer = "Unfortunately couldn't execute $toolName tool: ${exception.message}",
                        usedTools = usedTools
                    )
                }

                usedTools += toolName

                val secondPrompt = buildFollowUpPrompt(
                    userPrompt = request.prompt,
                    toolName = toolName,
                    toolResult = toolResult.take(MAX_TOOL_RESULT_CHARACTERS)
                )

                val secondResponse = llmClient.chat(secondPrompt)
                val secondDecision = normalizeDecision(
                    rawResponse = secondResponse,
                    parsedDecision = agentDecisionParser.parse(secondResponse)
                )

                AgentLoopResponse(
                    provider = request.provider,
                    answer = secondDecision.answer ?: secondResponse,
                    usedTools = usedTools
                )
            }

            else -> AgentLoopResponse(
                provider = request.provider,
                answer = fallbackAnswer(firstResponse),
                usedTools = usedTools
            )
        }
    }

    private fun normalizeDecision(
        rawResponse: String,
        parsedDecision: LlmAgentDecision
    ): LlmAgentDecision {
        val normalizedType = parsedDecision.type?.trim()?.lowercase()

        return when (normalizedType) {
            "tool_call" -> {
                if (parsedDecision.toolName.isNullOrBlank()) {
                    LlmAgentDecision(
                        type = "final_answer",
                        answer = fallbackAnswer(rawResponse)
                    )
                } else {
                    LlmAgentDecision(
                        type = "tool_call",
                        toolName = parsedDecision.toolName.trim(),
                        arguments = parsedDecision.arguments,
                        answer = null
                    )
                }
            }

            "final_answer" -> LlmAgentDecision(
                type = "final_answer",
                answer = parsedDecision.answer?.trim().takeUnless { it.isNullOrBlank() }
                    ?: fallbackAnswer(rawResponse)
            )

            else -> LlmAgentDecision(
                type = "final_answer",
                answer = fallbackAnswer(rawResponse)
            )
        }
    }

    private fun fallbackAnswer(rawResponse: String): String {
        return rawResponse.trim().ifBlank {
            "Model did not return a usable response."
        }
    }

    private fun buildInitialPrompt(userPrompt: String): String {
        val toolsDescription = toolRegistry.definitions()
            .joinToString(separator = "\n\n") { tool ->
                """
                Tool name: ${tool.name}
                Description: ${tool.description}
                Input schema:
                ${tool.inputSchema}
                """.trimIndent()
            }

        return """
            You are a coding agent that can decide whether to use a tool or return a final answer.

            User request:
            $userPrompt

            Available tools:
            $toolsDescription

            Respond ONLY with valid JSON in one of these two formats.

            If you want to use a tool:
            {
              "type": "tool_call",
              "toolName": "read_file",
              "arguments": {
                "path": "/workspace/...",
                "fromLine": 1,
                "toLine": 80,
                "maxCharacters": 3000
              }
            }

            If you already know the answer:
            {
              "type": "final_answer",
              "answer": "your final answer in the same language you were requested"
            }

            Do not add markdown, comments, explanations or code fences.
        """.trimIndent()
    }

    private fun buildFollowUpPrompt(
        userPrompt: String,
        toolName: String,
        toolResult: String
    ): String {
        return """
            You are a coding agent.

            Original user request:
            $userPrompt

            Tool used:
            $toolName

            Tool result:
            $toolResult

            Now respond ONLY with valid JSON:
            {
              "type": "final_answer",
              "answer": "your concise final answer in the same language you were requested"
            }

            Do not add markdown, comments, explanations or code fences.
        """.trimIndent()
    }
}