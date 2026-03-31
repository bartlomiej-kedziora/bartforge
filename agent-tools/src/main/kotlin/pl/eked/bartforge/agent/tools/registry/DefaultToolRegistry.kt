package pl.eked.bartforge.agent.tools.registry

import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.agent.ToolDefinition
import pl.eked.bartforge.agent.core.agent.ToolRegistry
import pl.eked.bartforge.agent.core.tool.ReadFileTool
import pl.eked.bartforge.agent.core.tool.ReadFileToolRequest
import pl.eked.bartforge.agent.tools.exception.ToolExecutionException

@Component
class DefaultToolRegistry(
    private val readFileTool: ReadFileTool
) : ToolRegistry {

    override fun definitions(): List<ToolDefinition> {
        return listOf(
            ToolDefinition(
                name = "read_file",
                description = "Reads a UTF-8 text file from disk.",
                inputSchema = """
                    {
                      "path": "string",
                      "fromLine": "integer, optional, default 1",
                      "toLine": "integer, optional",
                      "maxCharacters": "integer, optional, default 3000"
                    }
                """.trimIndent()
            )
        )
    }

    override fun execute(toolName: String, arguments: Map<String, Any?>): String {
        return when (toolName) {
            "read_file" -> executeReadFile(arguments)
            else -> throw ToolExecutionException("Unsupported tool: $toolName")
        }
    }

    private fun executeReadFile(arguments: Map<String, Any?>): String {
        val path = arguments["path"]?.toString()
            ?: throw ToolExecutionException("Missing required argument: path")

        val fromLine = arguments["fromLine"]?.toString()?.toIntOrNull() ?: 1
        val toLine = arguments["toLine"]?.toString()?.toIntOrNull()
        val maxCharacters = arguments["maxCharacters"]?.toString()?.toIntOrNull() ?: 3_000

        return readFileTool.execute(
            ReadFileToolRequest(
                path = path,
                fromLine = fromLine,
                toLine = toLine,
                maxCharacters = maxCharacters
            )
        ).content
    }
}