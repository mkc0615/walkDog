package walkdog.auth.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import walkdog.auth.config.security.JwtTokenProvider

@RestController
@RequestMapping("/auth")
class JwtController(
    private val jwtTokenProvider: JwtTokenProvider
) {
    @GetMapping("/.well-known/jwks.json")
    fun getJwks(): Map<String, Any> {
        val jwkSet = jwtTokenProvider.getJwkSet()
        return jwkSet.toJSONObject()
    }


}
