package walkdog.auth.controller

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import walkdog.auth.annotation.RequiresBasicAuth
import walkdog.auth.domain.model.dto.LoginRequest
import walkdog.auth.domain.model.dto.LoginResponse
import walkdog.auth.domain.repository.ClientRepository

@RestController
@RequestMapping("/auth/login")
class LoginController(
    private val environment: Environment,
    private val clientRepository: ClientRepository
) {
    @GetMapping
    @RequiresBasicAuth
    fun loginCheck(){
        println("logging in i guess")
    }
    @PostMapping
    @RequiresBasicAuth
    fun login(@RequestBody param: LoginRequest): LoginResponse {
        println("Login commencing !!!")
        return LoginResponse(
            code = "200",
            message = "success",
            email = param.email,
            status = "standard"
        )
    }
}
