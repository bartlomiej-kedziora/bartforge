package pl.eked.bartforge.agent.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.eked.bartforge.agent.api.dto.ListFilesRequestDto
import pl.eked.bartforge.agent.api.dto.ReadFileRequestDto
import pl.eked.bartforge.agent.api.dto.ToolResponseDto
import pl.eked.bartforge.agent.core.tool.ListFilesTool
import pl.eked.bartforge.agent.core.tool.ListFilesToolRequest
import pl.eked.bartforge.agent.core.tool.ReadFileTool
import pl.eked.bartforge.agent.core.tool.ReadFileToolRequest

@RestController
@RequestMapping("/api/tools")
class ToolController(
    private val listFilesTool: ListFilesTool,
    private val readFileTool: ReadFileTool
) {

    @PostMapping("/list-files")
    fun listFiles(@RequestBody request: ListFilesRequestDto): ToolResponseDto {
        val result = listFilesTool.execute(
            ListFilesToolRequest(
                rootPath = request.rootPath,
                recursive = request.recursive,
                maxDepth = request.maxDepth,
                includeDirectories = request.includeDirectories,
                excludedDirectoryNames = request.excludedDirectoryNames
            )
        )

        return ToolResponseDto(
            toolName = result.toolName,
            content = result.content
        )
    }

    @PostMapping("/read-file")
    fun readFile(@RequestBody request: ReadFileRequestDto): ToolResponseDto {
        val result = readFileTool.execute(
            ReadFileToolRequest(
                path = request.path,
                fromLine = request.fromLine,
                toLine = request.toLine,
                maxCharacters = request.maxCharacters
            )
        )

        return ToolResponseDto(
            toolName = result.toolName,
            content = result.content
        )
    }
}