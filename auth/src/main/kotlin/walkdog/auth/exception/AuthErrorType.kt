package walkdog.auth.exception

import org.springframework.security.oauth2.core.OAuth2Error

enum class AuthErrorType(override val code: String, override val message: String): BaseError {
    WRONG_PASSWORD("WRONG_PASSWORD", "Invalid password"),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
    CLIENT_ID_NOT_FOUND("CLIENT_NOT_FOUND", "client id not found"),

    BAD_AUTH_HEADER("BAD_AUTH_HEADER", "Authorization Header is not valid");
}

class AuthHeaderException: WalkDogAuthException(AuthErrorType.BAD_AUTH_HEADER)

interface BaseError {
    val code: String
    val message: String
}
