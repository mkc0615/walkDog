package walkdog.api.domain.appUsers

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.appUsers.model.AppUser

interface AppUserRepository: JpaRepository<AppUser, Long> {
    fun findByEmail(email: String): AppUser
}
