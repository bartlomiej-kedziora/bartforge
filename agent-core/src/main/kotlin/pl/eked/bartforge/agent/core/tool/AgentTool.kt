package pl.eked.bartforge.agent.core.tool

interface AgentTool<in T : Any> {
    fun name(): String
    fun execute(request: T): ToolResult
}