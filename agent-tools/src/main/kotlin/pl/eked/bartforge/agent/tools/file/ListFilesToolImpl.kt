package pl.eked.bartforge.agent.tools.file

import org.springframework.stereotype.Component
import pl.eked.bartforge.agent.core.AgentConstants
import pl.eked.bartforge.agent.core.tool.ListFilesTool
import pl.eked.bartforge.agent.core.tool.ListFilesToolRequest
import pl.eked.bartforge.agent.core.tool.ToolResult
import pl.eked.bartforge.agent.tools.exception.ToolExecutionException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

@Component
class ListFilesToolImpl : ListFilesTool {

    override fun name(): String = "list_files"

    override fun execute(request: ListFilesToolRequest): ToolResult {
        val rootPath = request.rootPath.ifBlank { AgentConstants.DEFAULT_WORKSPACE }
        val root = Paths.get(rootPath).normalize()

        validateRoot(root)

        try {
            val entries = if (request.recursive) {
                Files.walk(root, request.maxDepth).use { stream ->
                    stream
                        .filter { path -> path != root }
                        .filter { path -> shouldInclude(path, request) }
                        .map(root::relativize)
                        .map(Path::toString)
                        .sorted()
                        .toList()
                }
            } else {
                Files.list(root).use { stream ->
                    stream
                        .filter { path -> shouldInclude(path, request) }
                        .map(root::relativize)
                        .map(Path::toString)
                        .sorted()
                        .toList()
                }
            }

            val content = buildString {
                appendLine("Root: ${root.toAbsolutePath()}")
                appendLine("Entries: ${entries.size}")
                appendLine("Recursive: ${request.recursive}")
                appendLine("MaxDepth: ${request.maxDepth}")
                appendLine("IncludeDirectories: ${request.includeDirectories}")
                appendLine("ExcludedDirectories: ${request.excludedDirectoryNames.sorted().joinToString(",")}")
                appendLine()
                entries.forEach { appendLine(it) }
            }

            return ToolResult(
                toolName = name(),
                content = content.trim()
            )
        } catch (exception: Exception) {
            throw ToolExecutionException(
                "Failed to list files under path: ${root.toAbsolutePath()}",
                exception
            )
        }
    }

    private fun shouldInclude(path: Path, request: ListFilesToolRequest): Boolean {
        if (!request.includeDirectories && Files.isDirectory(path)) {
            return false
        }

        return !containsExcludedDirectory(path, request.excludedDirectoryNames)
    }

    private fun containsExcludedDirectory(path: Path, excludedDirectoryNames: Set<String>): Boolean {
        return path.any { segment -> excludedDirectoryNames.contains(segment.toString()) }
    }

    private fun validateRoot(root: Path) {
        if (!Files.exists(root)) {
            throw ToolExecutionException("Path does not exist: ${root.toAbsolutePath()}")
        }

        if (!root.isDirectory()) {
            throw ToolExecutionException("Path is not a directory: ${root.toAbsolutePath()}")
        }
    }
}