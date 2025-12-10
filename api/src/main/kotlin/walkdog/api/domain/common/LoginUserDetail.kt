package walkdog.api.domain.common

data class LoginUserDetail(
    val id: Long,
    val username: String,
    val ip: String,
    val mac: String,
) {
    companion object {
        fun create(id: Long, username: String, ip: String, mac: String): LoginUserDetail {
            return LoginUserDetail(id, username, ip, mac)
        }
    }
}