package walkdog.auth.config.security

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken
import java.io.Serial

class OAuth2PasswordGrantAuthenticationToken(
    val username: String,
    val password: String,
    clientPrincipal: Authentication,
    val scopes: Set<String>? = null
): OAuth2AuthorizationGrantAuthenticationToken(OAuth2PasswordGrantAuthenticationConverter.PASSWORD_GRANT_TYPE, clientPrincipal, null) {
    val clientPrincipal: Authentication = clientPrincipal

    companion object {
        @Serial
        private val serialVersionUID = 123456789L
    }
}
