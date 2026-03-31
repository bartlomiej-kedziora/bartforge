package pl.eked.bartforge.agent.api.dto

data class ListFilesRequestDto(
    val rootPath: String,
    val recursive: Boolean = true,
    val maxDepth: Int = 5,
    val includeDirectories: Boolean = false,
    val excludedDirectoryNames: Set<String> = setOf(
        ".git",
        ".gradle",
        ".idea",
        "build"
    )
)