package walkdog.api.domain.appUsers

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.appUsers.model.AppUserProfile

interface AppUserProfileRepository: JpaRepository<AppUserProfile, Long> {
    fun findByUserId(userId: Long): AppUserProfile
}