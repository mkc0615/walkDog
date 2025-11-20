package walkdog.auth.config.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Component
class JwtTokenProvider {
    private lateinit var keyStore: KeyStore
    private val keyAlias = "walkdog-oauth-jwt"
    private val keyPassword = "walkdogkpass".toCharArray()

    init {
        val keyStoreResource = ClassPathResource("walkdog.jks")
        val keyStorePassword = "walkdogspass".toCharArray()

        keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStoreResource.inputStream.use {
            keyStore.load(it, keyStorePassword)
        }
    }

    fun getJwkSet(): JWKSet {
        val privateKey = keyStore.getKey(keyAlias, keyPassword) as RSAPrivateKey
        val publicKey = keyStore.getCertificate(keyAlias).publicKey as RSAPublicKey

        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(keyAlias)
            .build()

        return JWKSet(rsaKey)
    }
}
