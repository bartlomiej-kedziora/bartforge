package pl.eked.bartforge.agent.core.agent

interface ToolRegistry {
    fun definitions(): List<ToolDefinition>
    fun execute(toolName: String, arguments: Map<String, Any?>): String
}