package walkdog.api.domain.appUsers.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import walkdog.api.domain.appUsers.model.dto.AppUserParam

@Entity
@Table(name = "app_users")
class AppUser(
    @Column(name = "username", nullable = false)
    var username: String,
    @Column(name = "password", nullable = false)
    var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    val isDeleted = "N"

    companion object {
        fun create(params: AppUserParam): AppUser {
            return AppUser(
                params.username,
                params.password
            )
        }
    }
}