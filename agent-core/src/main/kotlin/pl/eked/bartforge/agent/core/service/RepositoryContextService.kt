package pl.eked.bartforge.agent.core.service

import pl.eked.bartforge.agent.core.tool.ListFilesTool
import pl.eked.bartforge.agent.core.tool.ListFilesToolRequest
import pl.eked.bartforge.agent.core.tool.ReadFileTool
import pl.eked.bartforge.agent.core.tool.ReadFileToolRequest

class RepositoryContextService(
    private val listFilesTool: ListFilesTool,
    private val readFileTool: ReadFileTool
) {

    fun buildRepositoryContext(repositoryPath: String): RepositoryContext {
        val listResult = listFilesTool.execute(
            ListFilesToolRequest(
                rootPath = repositoryPath,
                recursive = true,
                maxDepth = 4,
                includeDirectories = false
            )
        )

        val files = extractFilePaths(listResult.content)
            .filter { isRelevantFile(it) }
            .take(12)

        val fileContents = files.mapNotNull { relativePath ->
            val fullPath = repositoryPath.trimEnd('/') + "/" + relativePath
            runCatching {
                readFileTool.execute(
                    ReadFileToolRequest(
                        path = fullPath,
                        fromLine = 1,
                        toLine = 200,
                        maxCharacters = 8_000
                    )
                ).content
            }.getOrNull()?.let { content ->
                RepositoryFileContext(
                    path = relativePath,
                    content = content
                )
            }
        }

        return RepositoryContext(
            files = files,
            fileContents = fileContents
        )
    }

    private fun extractFilePaths(content: String): List<String> {
        return content.lineSequence()
            .dropWhile { !it.startsWith("ExcludedDirectories:") }
            .drop(1)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()
    }

    private fun isRelevantFile(path: String): Boolean {
        return path.endsWith(".kt") ||
                path.endsWith(".kts") ||
                path.endsWith(".yml") ||
                path.endsWith(".yaml") ||
                path.endsWith(".md") ||
                path.endsWith(".properties") ||
                path.endsWith(".json")
    }
}

data class RepositoryContext(
    val files: List<String>,
    val fileContents: List<RepositoryFileContext>
)

data class RepositoryFileContext(
    val path: String,
    val content: String
)