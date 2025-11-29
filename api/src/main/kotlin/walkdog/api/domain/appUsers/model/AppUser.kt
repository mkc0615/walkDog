package walkdog.api.domain.appUsers.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import walkdog.api.domain.appUsers.model.dto.OwnerParam

@Entity
@Table(name = "app_users")
class AppUser(
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    val isDeleted = "N"

    fun update(params: OwnerParam) {
        this.email = params.email
        this.password = params.password
    }

    companion object {
        fun create(params: OwnerParam): AppUser {
            return AppUser(
                params.email,
                params.password
            )
        }
    }
}