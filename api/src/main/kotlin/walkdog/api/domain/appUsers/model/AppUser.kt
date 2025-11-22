package walkdog.api.domain.appUsers.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
class AppUser(
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    val isUsed = "Y"

    fun update(params: OwnerParam) {
        this.name = params.name
        this.email = params.email
        this.password = params.password
    }

    companion object {
        fun create(params: OwnerParam): AppUser {
            return AppUser(
                params.name,
                params.email,
                params.password
            )
        }
    }
}