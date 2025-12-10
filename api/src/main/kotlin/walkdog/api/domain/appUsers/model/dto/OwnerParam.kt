package walkdog.api.domain.appUsers.model.dto

data class OwnerParam(
    val username: String,
    val email: String,
    var password: String,
)
