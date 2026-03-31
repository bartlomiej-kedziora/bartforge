package pl.eked.bartforge.agent.core.agent

interface AgentDecisionParser {
    fun parse(rawResponse: String): LlmAgentDecision
}