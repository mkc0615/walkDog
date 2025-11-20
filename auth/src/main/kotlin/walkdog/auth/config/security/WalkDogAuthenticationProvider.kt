package walkdog.auth.config.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import org.springframework.stereotype.Service
import walkdog.auth.exception.AuthErrorType
import walkdog.auth.service.WalkDogUserDetailService

@Service
class WalkDogAuthenticationProvider(
    private val passwordEncoder: PasswordEncoder,
    private val tokenGenerator: OAuth2TokenGenerator<out OAuth2Token>,
    private val walkDogUserDetailService: WalkDogUserDetailService,
    private val authorizationService: OAuth2AuthorizationService,
) {

    fun authenticate(
        registeredClient: RegisteredClient,
        authentication: Authentication
    ): Authentication {
        val grantAuthenticationToken: OAuth2PasswordGrantAuthenticationToken = authentication as OAuth2PasswordGrantAuthenticationToken

        val clientPrincipal = grantAuthenticationToken.clientPrincipal
        val client = registeredClient

        val username = grantAuthenticationToken.username
        val password = grantAuthenticationToken.password

        val userDetails = walkDogUserDetailService.loadUserByUsername(username)
        if (!passwordEncoder.matches(password, userDetails.password)) {
            throw OAuth2AuthenticationException(
                OAuth2Error(
                    AuthErrorType.WRONG_PASSWORD.code,
                    AuthErrorType.WRONG_PASSWORD.message,
                    ""
                )
            )
        }

        if (registeredClient.scopes.contains("walkdog") == true) {
            // no scope check needed
        }

        val userPrincipal = UsernamePasswordAuthenticationToken(
            userDetails,
            grantAuthenticationToken.password,
            registeredClient.scopes?.map { SimpleGrantedAuthority(it) } ?: emptyList(),
        )

        val tokenContext: OAuth2TokenContext = DefaultOAuth2TokenContext.builder()
            .registeredClient(client)
            .principal(userPrincipal)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .tokenType(OAuth2TokenType.ACCESS_TOKEN)
            .authorizationGrantType(grantAuthenticationToken.getGrantType())
            .authorizationGrant(grantAuthenticationToken)
            .build()

        val generatedAccessToken = tokenGenerator.generate(tokenContext)
        if (generatedAccessToken == null) {
            val error = OAuth2Error(
                OAuth2ErrorCodes.SERVER_ERROR,
                "The Token generator failed to generate the access token",
                null
            )
            throw OAuth2AuthenticationException(error)
        }

        val accessToken = OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.tokenValue,
            generatedAccessToken.issuedAt,
            generatedAccessToken.expiresAt,
            registeredClient.scopes
        )

        val authorizationBuilder = OAuth2Authorization.withRegisteredClient(client)
            .principalName(userPrincipal.name)
            .authorizationGrantType(grantAuthenticationToken.getGrantType())

        if (generatedAccessToken is ClaimAccessor) {
            authorizationBuilder.token(accessToken) {
                metaData: MutableMap<String?, Any?> -> {
                    metaData[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] = (generatedAccessToken as ClaimAccessor).claims
                }
            }
        } else {
            authorizationBuilder.accessToken(accessToken)
        }

        val authorization = authorizationBuilder.build()
        authorizationService.save(authorization)

        return OAuth2AccessTokenAuthenticationToken(client, clientPrincipal, accessToken)
    }
}
