package pl.eked.bartforge.agent.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.eked.bartforge.agent.core.port.LlmClient
import pl.eked.bartforge.agent.core.service.AgentService
import pl.eked.bartforge.agent.core.service.FileAnalysisService
import pl.eked.bartforge.agent.core.service.LlmClientRegistry
import pl.eked.bartforge.agent.core.service.RepositoryAnalysisService
import pl.eked.bartforge.agent.core.service.RepositoryContextService
import pl.eked.bartforge.agent.core.tool.ListFilesTool
import pl.eked.bartforge.agent.core.tool.ReadFileTool

@Configuration
class CoreConfig {

    @Bean
    fun llmClientRegistry(clients: List<LlmClient>): LlmClientRegistry {
        return LlmClientRegistry(clients)
    }

    @Bean
    fun agentService(
        llmClientRegistry: LlmClientRegistry
    ): AgentService {
        return AgentService(llmClientRegistry)
    }

    @Bean
    fun repositoryContextService(
        listFilesTool: ListFilesTool,
        readFileTool: ReadFileTool
    ): RepositoryContextService {
        return RepositoryContextService(listFilesTool, readFileTool)
    }

    @Bean
    fun repositoryAnalysisService(
        llmClientRegistry: LlmClientRegistry,
        repositoryContextService: RepositoryContextService
    ): RepositoryAnalysisService {
        return RepositoryAnalysisService(llmClientRegistry, repositoryContextService)
    }

    @Bean
    fun fileAnalysisService(
        llmClientRegistry: LlmClientRegistry,
        readFileTool: ReadFileTool
    ): FileAnalysisService {
        return FileAnalysisService(llmClientRegistry, readFileTool)
    }
}