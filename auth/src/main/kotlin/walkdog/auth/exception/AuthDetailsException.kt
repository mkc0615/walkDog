package walkdog.auth.exception

import org.springframework.security.oauth2.core.OAuth2AuthenticationException

open class AuthDetailsException(
    val errorCode: String,
    override val message: String,
    val details: List<Any> = emptyList()
): OAuth2AuthenticationException(errorCode)
