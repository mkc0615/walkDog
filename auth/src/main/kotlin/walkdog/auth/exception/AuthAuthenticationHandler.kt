package walkdog.auth.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class AuthAuthenticationHandler: AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val errorDetails = extractErrorDetails(exception)
        response.apply {
            status = HttpStatus.BAD_REQUEST.value()
            characterEncoding = "UTF-8"
            contentType = "${MediaType.APPLICATION_JSON_VALUE}; charset=utf-8"

            val errorResponse = GeneralErrorResponse(
                code = errorDetails.code,
                message = errorDetails.message,
                details = errorDetails.details
            )

            ObjectMapper().writeValue(writer, errorResponse)
        }
    }

    private fun extractErrorDetails(exception: AuthenticationException): ErrorDetails {
        return when (exception) {
            is AuthDetailsException -> {
                ErrorDetails(
                    exception.errorCode,
                    exception.message,
                    exception.details
                )
            }
            is OAuth2AuthenticationException -> {
                val oauth2Error = exception.error
                ErrorDetails(
                    oauth2Error.errorCode,
                    exception.message ?: "unknown error",
                    emptyList()
                )
            }
            else -> ErrorDetails(
                "UNKNOWN_ERROR",
                exception.message ?: "Unknown error",
                emptyList()
            )
        }
    }
}

data class ErrorDetails(
    val code: String,
    val message: String,
    val details: List<Any> = emptyList()
)