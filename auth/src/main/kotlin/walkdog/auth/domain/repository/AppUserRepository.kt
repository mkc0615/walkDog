package walkdog.auth.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.auth.domain.model.entity.AppUser

interface AppUserRepository : JpaRepository<AppUser, String> {
    fun findByUsername(username: String): AppUser?
}