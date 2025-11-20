package walkdog.auth.config.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import walkdog.auth.domain.repository.JpaRegisteredClientRepository
import java.util.Base64

@Component
class BasicAuthValidator(
    private val jpaRegisteredClientRepository: JpaRegisteredClientRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun isValidBasicAuth(authorization: String?): Boolean {
        if (authorization == null || !authorization.startsWith("Basic")) {
            return false
        }

        val base64Creds = authorization.substring(6)
        val creds = String(Base64.getDecoder().decode(base64Creds)).split(":")

        if (creds.size != 2) {
            return false
        }

        val clientId = creds[0]
        val clientSecret = creds[1]

        val registeredClient = jpaRegisteredClientRepository.findByClientId(clientId) ?: return false
        return passwordEncoder.matches(clientSecret, registeredClient.clientSecret)
    }
}