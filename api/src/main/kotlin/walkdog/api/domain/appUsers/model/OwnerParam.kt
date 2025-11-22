package walkdog.api.domain.appUsers.model

data class OwnerParam(
    val name: String,
    val email: String,
    var password: String,
)
