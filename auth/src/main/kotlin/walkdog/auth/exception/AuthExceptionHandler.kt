package walkdog.auth.exception

class AuthExceptionHandler {
}

data class GeneralErrorResponse(
    val code: String,
    val message: String,
    val details: List<Any> = emptyList()
)