package walkdog.api.service.users

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.appUsers.AppUserProfileRepository
import walkdog.api.domain.appUsers.AppUserRepository
import walkdog.api.domain.appUsers.model.dto.AppUserResponse

@Service
@Transactional
class AppUserQuery(
    private val appUserRepository: AppUserRepository,
    private val appUserProfileRepository: AppUserProfileRepository,
) {
    fun getAppUserProfile(appUserId: Long): AppUserResponse {
        val user = appUserRepository.findById(appUserId).orElseThrow {
            throw IllegalArgumentException("App user $appUserId not found")
        }
        val profile = appUserProfileRepository.findByUserId(appUserId)
        return AppUserResponse.create(user, profile)
    }
}
