package walkdog.auth.config.security

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import walkdog.auth.exception.AuthHeaderException

@Aspect
@Component
class BasicAuthAspect(
    private val authValidator: BasicAuthValidator,
    private val request: HttpServletRequest
) {
    @Before("@annotation(walkdog.auth.annotation.RequiresBasicAuth)")
    fun checkBasicAuth() {
        val authorization = request.getHeader("Authorization")

        if (!authValidator.isValidBasicAuth(authorization)) {
            throw AuthHeaderException()
        }
    }
}