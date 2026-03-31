package pl.eked.bartforge.agent.llm.exception

class OpenAiException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)