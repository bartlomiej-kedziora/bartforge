package pl.eked.bartforge.agent.tools.file

import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.tool.ReadFileTool
import pl.eked.bartforge.agent.core.tool.ReadFileToolRequest
import pl.eked.bartforge.agent.core.tool.ToolResult
import pl.eked.bartforge.agent.tools.exception.ToolExecutionException
import java.nio.charset.MalformedInputException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.text.Charsets.UTF_8

@Component
class ReadFileToolImpl : ReadFileTool {

    override fun name(): String = "read_file"

    override fun execute(request: ReadFileToolRequest): ToolResult {
        val path = Paths.get(request.path).normalize()

        validatePath(path)
        validateRequest(request)

        try {
            val allLines = Files.readAllLines(path, UTF_8)
            val totalLines = allLines.size
            val effectiveToLine = request.toLine ?: totalLines

            if (request.fromLine > totalLines) {
                throw ToolExecutionException(
                    "Requested fromLine ${request.fromLine} exceeds file length $totalLines for file: ${path.toAbsolutePath()}"
                )
            }

            val selectedLines = allLines
                .subList(request.fromLine - 1, effectiveToLine.coerceAtMost(totalLines))
                .mapIndexed { index, line ->
                    val lineNumber = request.fromLine + index
                    "${lineNumber.toString().padStart(4, ' ')} | $line"
                }

            val renderedContent = selectedLines.joinToString(separator = System.lineSeparator())
            val truncatedContent = renderedContent.take(request.maxCharacters)
            val truncated = renderedContent.length > truncatedContent.length

            val content = buildString {
                appendLine("Path: ${path.toAbsolutePath()}")
                appendLine("TotalLines: $totalLines")
                appendLine("FromLine: ${request.fromLine}")
                appendLine("ToLine: ${effectiveToLine.coerceAtMost(totalLines)}")
                appendLine("Characters: ${renderedContent.length}")
                appendLine("Truncated: $truncated")
                appendLine()
                append(truncatedContent)
            }

            return ToolResult(
                toolName = name(),
                content = content
            )
        } catch (exception: MalformedInputException) {
            throw ToolExecutionException(
                "File is not readable as UTF-8 text: ${path.toAbsolutePath()}",
                exception
            )
        } catch (exception: ToolExecutionException) {
            throw exception
        } catch (exception: Exception) {
            throw ToolExecutionException(
                "Failed to read file: ${path.toAbsolutePath()}",
                exception
            )
        }
    }

    private fun validatePath(path: Path) {
        if (!Files.exists(path)) {
            throw ToolExecutionException("File does not exist: ${path.toAbsolutePath()}")
        }

        if (!path.isRegularFile()) {
            throw ToolExecutionException("Path is not a regular file: ${path.toAbsolutePath()}")
        }
    }

    private fun validateRequest(request: ReadFileToolRequest) {
        if (request.fromLine < 1) {
            throw ToolExecutionException("fromLine must be greater than or equal to 1")
        }

        if (request.toLine != null && request.toLine!! < request.fromLine) {
            throw ToolExecutionException("toLine must be greater than or equal to fromLine")
        }

        if (request.maxCharacters < 1) {
            throw ToolExecutionException("maxCharacters must be greater than 0")
        }
    }
}