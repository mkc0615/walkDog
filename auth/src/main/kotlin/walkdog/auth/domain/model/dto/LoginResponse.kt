package walkdog.auth.domain.model.dto

data class LoginResponse(
    val code: String,
    val message: String,
    val email: String,
    val status: String
)