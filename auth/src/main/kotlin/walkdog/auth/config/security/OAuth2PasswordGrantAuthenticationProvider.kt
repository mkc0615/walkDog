package walkdog.auth.config.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationContext
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import walkdog.auth.exception.AuthErrorType

class OAuth2PasswordGrantAuthenticationProvider(
    private val registeredClientRepository: RegisteredClientRepository,
    private val walkDogAuthenticationProvider: WalkDogAuthenticationProvider
): AuthenticationProvider {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication?): Authentication? {
        val grantAuthenticationToken: OAuth2PasswordGrantAuthenticationToken = authentication as OAuth2PasswordGrantAuthenticationToken
        val clientPrincipal = grantAuthenticationToken.clientPrincipal
        val client = (clientPrincipal as OAuth2ClientAuthenticationToken).registeredClient

        val registeredClient = registeredClientRepository.findByClientId(client?.clientId) ?: throw OAuth2AuthenticationException(
            OAuth2Error(
                AuthErrorType.CLIENT_ID_NOT_FOUND.code,
                AuthErrorType.CLIENT_ID_NOT_FOUND.message,
                ""
            )
        )

        return walkDogAuthenticationProvider.authenticate(
                registeredClient,
                authentication
        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return OAuth2PasswordGrantAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    companion object {
        const val OAUTH2_CLIENT_ID_WALKDOG = "walkdog_api"
    }
}
