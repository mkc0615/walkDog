package walkdog.api.service.users

import org.springframework.security.crypto.password.PasswordEncoder
import walkdog.api.config.WalkDogPasswordEncoder
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppUserCommandTest {
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeTest
    fun setup() {
        passwordEncoder = WalkDogPasswordEncoder()
    }

    @Test
    fun encodingTest() {
        val password = "test123"

        val result = passwordEncoder.encode(password)

        println("result  :::::  $result")
    }

}