package walkdog.api.domain.appUsers.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "app_user_profiles")
class AppUserProfile(
    @Column
    var userId: Long,
    @Column
    var email: String,
    @Column
    var weight: Double,
    @Column
    var imageUrl: String,
    @Column
    var remarks: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}