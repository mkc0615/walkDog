package walkdog.auth.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import walkdog.auth.domain.model.entity.AppUser
import walkdog.auth.domain.model.entity.Authorization
import walkdog.auth.domain.repository.AuthorizationRepository
import java.util.Optional
import javax.swing.text.html.Option
import kotlin.math.PI

@Component
class JpaOAuth2AuthorizationService(
    private val authorizationRepository: AuthorizationRepository,
    private val registeredClientRepository: RegisteredClientRepository
): OAuth2AuthorizationService {

    private val objectMapper = ObjectMapper()

    init {
        Assert.notNull(authorizationRepository, "AuthorizationRepository must not be null")
        Assert.notNull(registeredClientRepository, "RegisteredClientRepository must not be null")

        val classLoader = JpaOAuth2AuthorizationService::class.java.classLoader
        val securityModules = SecurityJackson2Modules.getModules(classLoader)
        objectMapper.registerModules(securityModules)
        objectMapper.registerModules(OAuth2AuthorizationServerJackson2Module())
    }

    override fun save(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "Authorization must not be null")
        authorizationRepository.save<Authorization>(toEntity(authorization))
    }

    override fun remove(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "Authorization must not be null")
        authorizationRepository.deleteById(authorization.id)
    }

    override fun findById(id: String): OAuth2Authorization? {
        Assert.hasText(id, "id cannot be empty")
        return authorizationRepository.findById(id)
            .map { entity: Authorization -> this.toObject(entity) }.orElse(null)
    }

    override fun findByToken(
        token: String,
        tokenType: OAuth2TokenType?
    ): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")
        val result: Optional<Authorization> = if (tokenType != null && OAuth2ParameterNames.ACCESS_TOKEN == tokenType.value) {
            authorizationRepository.findByAccessTokenValue(token)
        } else {
            Optional.empty()
        }

        return result.map { entity: Authorization -> this.toObject(entity) }.orElse(null)
    }

    private fun toEntity(authorization: OAuth2Authorization): Authorization {
        val entity = Authorization()

        entity.id = authorization.id
        entity.registeredClientId = authorization.registeredClientId
        entity.principalName = authorization.principalName
        entity.authorizationGrantType = authorization.authorizationGrantType.value
        entity.authorizedScopes = StringUtils.collectionToDelimitedString(authorization.authorizedScopes, ",")
        entity.attributes = writeMap(authorization.attributes)
        entity.state = authorization.getAttribute(OAuth2ParameterNames.STATE)

        val accessToken = authorization.getToken(OAuth2AccessToken::class.java)
        if (accessToken != null) {
            entity.accessTokenValue = accessToken.token.tokenValue
            entity.accessTokenIssuedAt = accessToken.token.issuedAt
            entity.accessTokenExpiresAt = accessToken.token.expiresAt
            entity.accessTokenMetadata = writeMap(accessToken.metadata)

            if (accessToken.token.scopes != null) {
                entity.accessTokenScopes = StringUtils.collectionToDelimitedString(accessToken.token.scopes, ",")

            }
        }
        return entity
    }

    private fun toObject(entity: Authorization): OAuth2Authorization {
        val registeredClient = registeredClientRepository.findById(entity.registeredClientId)
            ?: throw DataRetrievalFailureException("RegisteredClient not found ::: ${entity.registeredClientId}")

        val builder = OAuth2Authorization.withRegisteredClient(registeredClient)
            .id(entity.id)
            .principalName(entity.principalName)
            .authorizationGrantType(resolveAuthorizationGrantType(entity.authorizationGrantType))
            .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.authorizedScopes))
            .attributes { attributes: MutableMap<String?, Any?> ->
                attributes.putAll(parseMap(entity.attributes))
            }

        if (entity.state != null) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.state)
        }
        if(entity.accessTokenValue != null) {
            val accessToken = OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                entity.accessTokenValue,
                entity.accessTokenIssuedAt,
                entity.accessTokenExpiresAt,
                StringUtils.commaDelimitedListToSet(entity.accessTokenScopes),
            )

            builder.token(accessToken) { metaData: MutableMap<String?, Any?> ->
                metaData.putAll(parseMap(entity.accessTokenMetadata))
            }
        }
        return builder.build()
    }

    private fun writeMap(metaData: Map<String, Any>?): String {
        try {
            return objectMapper.writeValueAsString(metaData)
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message, ex)
        }
    }

    private fun parseMap(data: String): Map<String, Any> {
        return try {
            objectMapper.readValue(data, object : TypeReference<Map<String, Any>>() {})
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message, ex)
        }
    }

    companion object {
        private fun resolveAuthorizationGrantType(authorizationGrantType: String): AuthorizationGrantType {
            if (AuthorizationGrantType.AUTHORIZATION_CODE.value == authorizationGrantType) {
                return AuthorizationGrantType.AUTHORIZATION_CODE
            } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.value == authorizationGrantType) {
                return AuthorizationGrantType.CLIENT_CREDENTIALS
            } else if (AuthorizationGrantType.REFRESH_TOKEN.value == authorizationGrantType) {
                return AuthorizationGrantType.REFRESH_TOKEN
            } else if (AuthorizationGrantType.DEVICE_CODE.value == authorizationGrantType) {
                return AuthorizationGrantType.DEVICE_CODE
            }
            return AuthorizationGrantType(authorizationGrantType)
        }
    }
}