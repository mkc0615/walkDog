package walkdog.auth.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.token.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import walkdog.auth.config.security.OAuth2PasswordGrantAuthenticationConverter
import walkdog.auth.config.security.OAuth2PasswordGrantAuthenticationProvider
import walkdog.auth.config.security.WalkDogAuthenticationProvider
import walkdog.auth.config.security.WalkDogPasswordEncoder
import walkdog.auth.exception.AuthAuthenticationHandler
import walkdog.auth.service.WalkDogUserDetailService
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun authorizationFilterChain(
        http: HttpSecurity,
        registeredClientRepository: RegisteredClientRepository,
        walkDogAuthenticationProvider: WalkDogAuthenticationProvider,
        @Qualifier("walkDogUserDetailService")
        walkDogUserDetailService: WalkDogUserDetailService,
        tokenGenerator: OAuth2TokenGenerator<*>
    ): SecurityFilterChain {
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()

        authorizationServerConfigurer
            .tokenEndpoint { tokenEndPoint: OAuth2TokenEndpointConfigurer ->
               tokenEndPoint
                   .accessTokenRequestConverters { converters: MutableList<AuthenticationConverter> ->
                       converters.add(OAuth2PasswordGrantAuthenticationConverter())
                   }
                   .authenticationProviders { providers: MutableList<AuthenticationProvider> ->
                       providers.add(
                           OAuth2PasswordGrantAuthenticationProvider(
                               registeredClientRepository,
                               walkDogAuthenticationProvider,
                           )
                       )
                   }
                   .errorResponseHandler(AuthAuthenticationHandler())
            }

        val endpointsMatcher = authorizationServerConfigurer.endpointsMatcher

        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .securityMatcher(endpointsMatcher)
            .authorizeHttpRequests { authorize ->
                authorize
                    .anyRequest().authenticated()
            }
            .csrf { csrf: CsrfConfigurer<HttpSecurity?> -> csrf.ignoringRequestMatchers(endpointsMatcher) }
            .with(authorizationServerConfigurer, Customizer.withDefaults())

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return WalkDogPasswordEncoder()
    }

    @Bean
    fun tokenGenerator(
        jwtSource: JWKSource<SecurityContext?>?,
        oAuth2TokenCustomizer: OAuth2TokenCustomizer<JwtEncodingContext?>?
    ) : OAuth2TokenGenerator<*> {
        val jwtGenerator = JwtGenerator(NimbusJwtEncoder(jwtSource))
        jwtGenerator.setJwtCustomizer(oAuth2TokenCustomizer)
        val accessTokenGenerator = OAuth2AccessTokenGenerator()
        return DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator)
    }
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyStoreResource = ClassPathResource("walkdog.jks")
        val keyStorePassword = "walkdogspass".toCharArray()
        val keyAlias = "walkdog-oauth-jwt"
        val keyPassword = "walkdogkpass".toCharArray()

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStoreResource.inputStream.use {
            keyStore.load(it, keyStorePassword)
        }

        val privateKey = keyStore.getKey(keyAlias, keyPassword) as RSAPrivateKey
        val publicKey = keyStore.getCertificate(keyAlias).publicKey as RSAPublicKey

        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(keyAlias)
            .build()

        val jwkSet = JWKSet(rsaKey)
        return ImmutableJWKSet(jwkSet)
    }

    @Bean
    fun oAuth2TokenCustomizer(
        userDetailsService: UserDetailsService,
    ): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer<JwtEncodingContext> { context: JwtEncodingContext ->
            if(
                (OAuth2PasswordGrantAuthenticationConverter.PASSWORD_GRANT_TYPE == context.authorizationGrantType) &&
                (OAuth2TokenType.ACCESS_TOKEN == context.tokenType)
            ) {
                val principal: Authentication = context.getPrincipal()
                val authorities = principal.authorities
                    .map { it.authority }
                    .toSet()
                context.claims.claim("authorities", authorities)
            }
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.addAllowedOriginPattern("*")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}
