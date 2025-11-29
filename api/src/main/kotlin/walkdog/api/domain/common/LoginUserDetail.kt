package walkdog.api.domain.common

data class LoginUserDetail(
    val id: Long,
    val email: String,
    val ip: String,
    val mac: String,
) {
    companion object {
        fun create(id: Long, email: String, ip: String, mac: String): LoginUserDetail {
            return LoginUserDetail(id, email, ip, mac)
        }
    }
}