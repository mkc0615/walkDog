package walkdog.auth.domain.model.dto

data class LoginRequest (
    val email: String,
    val password: String
)
