package walkdog.auth.domain.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.ObjectUtils
import org.springframework.util.StringUtils
import walkdog.auth.domain.model.entity.Client
import java.time.Duration
import java.util.function.Consumer

@Component
class JpaRegisteredClientRepository(
    private val clientRepository: ClientRepository
): RegisteredClientRepository {

    private val objectMapper = ObjectMapper()

    init {
        Assert.notNull(clientRepository, "Client repository must not be null")

        val classLoader = JpaRegisteredClientRepository::class.java.classLoader
        val securityModules = SecurityJackson2Modules.getModules(classLoader)
        objectMapper.registerModules(securityModules)
        objectMapper.registerModules(OAuth2AuthorizationServerJackson2Module())
    }

    override fun save(registeredClient: RegisteredClient) {
        Assert.notNull(registeredClient, "registeredClient must not be null")
        clientRepository.save<Client>(toEntity(registeredClient))
    }

    override fun findById(id: String): RegisteredClient? {
        Assert.hasText(id, "id must not be null")
        return clientRepository.findById(id)
            .map { client: Client -> this.toObject(client) }
            .orElse(null)
    }

    override fun findByClientId(clientId: String): RegisteredClient? {
        Assert.hasText(clientId, "clientId must not be null")
        return clientRepository.findByClientId(clientId)
            .map { client: Client ->
                this.toObject(client)
            }.orElse(null)
    }

    private fun toEntity(registeredClient: RegisteredClient): Client {
        val clientAuthenticationMethods: MutableList<String> = ArrayList(registeredClient.clientAuthenticationMethods.size)
        registeredClient.clientAuthenticationMethods.forEach({ clientAuthenticationMethod: ClientAuthenticationMethod ->
            clientAuthenticationMethods.add(clientAuthenticationMethod.value)
        })

        val authorizationGrantTypes: MutableList<String> = ArrayList(registeredClient.authorizationGrantTypes.size)
        registeredClient.authorizationGrantTypes.forEach(Consumer { authorizationGrantType: AuthorizationGrantType ->
            authorizationGrantTypes.add(authorizationGrantType.value)
        })

        val entity = Client()
        entity.id = registeredClient.id
        entity.clientId = registeredClient.clientId
        entity.clientIdIssuedAt = registeredClient.clientIdIssuedAt
        entity.clientSecret = registeredClient.clientSecret
        entity.clientSecretExpiresAt = registeredClient.clientSecretExpiresAt
        entity.clientName = registeredClient.clientName
        entity.clientAuthenticationMethods = StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods)
        entity.authorizationGrantTypes = StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes)
        entity.redirectUris = StringUtils.collectionToCommaDelimitedString(registeredClient.redirectUris)
        entity.postLogoutRedirectUris = StringUtils.collectionToCommaDelimitedString(registeredClient.postLogoutRedirectUris)
        entity.scopes = StringUtils.collectionToCommaDelimitedString(registeredClient.scopes)
        entity.clientSettings = writeMap(registeredClient.clientSettings.settings)
        entity.tokenSettings = writeMap(registeredClient.tokenSettings.settings)

        return entity
    }

    private fun toObject(client: Client): RegisteredClient {
        val clientAuthenticationMethods: Set<String> = StringUtils.commaDelimitedListToSet(
            client.clientAuthenticationMethods
        )
        val authorizationGrantTypes: Set<String> = StringUtils.commaDelimitedListToSet(
            client.authorizationGrantTypes
        )
        val redirectUris: Set<String?> = StringUtils.commaDelimitedListToSet(
            client.redirectUris
        )
        val postLogoutRedirectUris: Set<String?> = StringUtils.commaDelimitedListToSet(
            client.postLogoutRedirectUris
        )
        val clientScopes: Set<String?> = StringUtils.commaDelimitedListToSet(
            client.scopes
        )

        val builder = RegisteredClient.withId(client.id)
            .clientId(client.clientId)
            .clientIdIssuedAt(client.clientIdIssuedAt)
            .clientSecret(client.clientSecret)
            .clientSecretExpiresAt(client.clientSecretExpiresAt)
            .clientName(client.clientName)
            .clientAuthenticationMethods { authenticationMethods: MutableSet<ClientAuthenticationMethod?> ->
                clientAuthenticationMethods.forEach(
                    Consumer { authenticationMethod: String ->
                        authenticationMethods.add(
                            resolveClientAuthenticationMethod(authenticationMethod)
                        )
                    })
            }
            .authorizationGrantTypes { grantTypes: MutableSet<AuthorizationGrantType?> ->
                authorizationGrantTypes.forEach(
                    Consumer { grantType: String ->
                        grantTypes.add(
                            resolveAuthorizationGrantType(grantType)
                        )
                    })
            }
            .redirectUris { uris: MutableSet<String?> -> uris.addAll(redirectUris) }
            .postLogoutRedirectUris { uris: MutableSet<String?> ->
                uris.addAll(
                    postLogoutRedirectUris
                )
            }
            .scopes { scopes: MutableSet<String?> -> scopes.addAll(clientScopes) }

        if (!ObjectUtils.isEmpty(client.tokenSettings)) {
            val tokenSettingsMap = parseMap(client.tokenSettings)
            if (tokenSettingsMap.containsKey("access_token_live_days")) {
                builder.tokenSettings(
                    TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofDays((tokenSettingsMap.get("access_token_live_days") as Int).toLong())).build())
            }
        }

        return builder.build()
    }

    private fun parseMap(data: String): Map<String, Any> {
        return try {
            objectMapper.readValue(data, object : TypeReference<Map<String, Any>>() {})
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message, ex)
        }
    }

    private fun writeMap(data: Map<String, Any>): String {
        try {
            return objectMapper.writeValueAsString(data)
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message, e)
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
            }
            return AuthorizationGrantType(authorizationGrantType) // Custom authorization grant type
        }

        private fun resolveClientAuthenticationMethod(clientAuthenticationMethod: String): ClientAuthenticationMethod {
            if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.value == clientAuthenticationMethod) {
                return ClientAuthenticationMethod.CLIENT_SECRET_BASIC
            } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.value == clientAuthenticationMethod) {
                return ClientAuthenticationMethod.CLIENT_SECRET_POST
            } else if (ClientAuthenticationMethod.NONE.value == clientAuthenticationMethod) {
                return ClientAuthenticationMethod.NONE
            }
            return ClientAuthenticationMethod(clientAuthenticationMethod) // Custom client authentication method
        }
    }
}