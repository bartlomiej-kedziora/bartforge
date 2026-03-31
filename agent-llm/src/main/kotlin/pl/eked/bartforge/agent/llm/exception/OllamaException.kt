package pl.eked.bartforge.agent.llm.exception

class OllamaException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)