package pl.eked.bartforge.agent.api.agent

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.agent.AgentDecisionParser
import pl.eked.bartforge.agent.core.agent.LlmAgentDecision

@Component
class JacksonAgentDecisionParser(
    private val objectMapper: ObjectMapper
) : AgentDecisionParser {

    override fun parse(rawResponse: String): LlmAgentDecision {
        val candidates = listOfNotNull(
            rawResponse,
            extractJsonCodeBlock(rawResponse),
            extractFirstJsonObject(rawResponse)
        ).distinct()

        candidates.forEach { candidate ->
            runCatching {
                return objectMapper.readValue(candidate, LlmAgentDecision::class.java)
            }
        }

        return LlmAgentDecision(
            type = "final_answer",
            answer = rawResponse
        )
    }

    private fun extractJsonCodeBlock(rawResponse: String): String? {
        val regex = Regex("```json\\s*(\\{.*?})\\s*```", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        return regex.find(rawResponse)?.groupValues?.getOrNull(1)
    }

    private fun extractFirstJsonObject(rawResponse: String): String? {
        val start = rawResponse.indexOf('{')
        val end = rawResponse.lastIndexOf('}')

        if (start == -1 || end == -1 || end <= start) {
            return null
        }

        return rawResponse.substring(start, end + 1)
    }
}