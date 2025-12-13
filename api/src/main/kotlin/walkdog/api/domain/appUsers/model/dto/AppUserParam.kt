package walkdog.api.domain.appUsers.model.dto

data class AppUserParam(
    val username: String,
    val email: String,
    var password: String,
)
