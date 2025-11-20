package walkdog.auth.config.security

import org.springframework.security.crypto.password.PasswordEncoder
import java.security.MessageDigest

class WalkDogPasswordEncoder: PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String {
        requireNotNull(rawPassword) {
            "rawPassword should not be null"
        }

        if (rawPassword.toString().startsWith("{noop}")) {
            return rawPassword.toString()
        }
        return getSHA512PW(rawPassword)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        requireNotNull(rawPassword) {
            "rawPassword should not be null"
        }
        if (encodedPassword.isNullOrEmpty()) {
            return false
        }
        if(encodedPassword.startsWith("{noop}")) {
            return rawPassword.toString() == encodedPassword.substring(6)
        }
        return encodedPassword == getSHA512PW(rawPassword)
    }

    private fun getSHA512PW(rawPassword: CharSequence?): String {
        val md = MessageDigest.getInstance("SHA-512")
        md.update(rawPassword.toString().toByteArray())
        val msgb = md.digest()
        return msgb.joinToString("") { "%02x".format(it) }
    }
}
