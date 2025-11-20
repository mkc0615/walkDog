package walkdog.api.config

import org.springframework.security.crypto.password.PasswordEncoder
import java.security.MessageDigest

class FreshPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String {
        requireNotNull(rawPassword) {
            "rawPassword is required"
        }
        if (rawPassword.toString().startsWith("{noop}")) {
            return rawPassword.toString()
        }
        return rawPassword.toString()
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        requireNotNull(rawPassword) {
            "rawPassword is required"
        }
        if (encodedPassword.isNullOrEmpty()) {
            return false
        }
        if(encodedPassword.startsWith("{noop}")) {
            return encodedPassword.toString() == rawPassword.toString()
        }
        return encodedPassword == getSHA512Pw(rawPassword)
    }

    private fun getSHA512Pw(rawPassword: CharSequence?): String {
        val md: MessageDigest = MessageDigest.getInstance("SHA-512")
        md.update(rawPassword.toString().toByteArray())
        val msgb = md.digest()

        return msgb.joinToString("") {
            "%02x".format(it)
        }
    }
}