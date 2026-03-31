package pl.eked.bartforge.agent.tools.exception

class ToolExecutionException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)