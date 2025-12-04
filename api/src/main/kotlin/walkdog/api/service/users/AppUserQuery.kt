package walkdog.api.service.users

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.appUsers.AppUserProfileRepository
import walkdog.api.domain.appUsers.AppUserRepository
import walkdog.api.domain.appUsers.model.dto.OwnerResponse
import walkdog.api.domain.dogs.repository.DogRepository

@Service
@Transactional
class AppUserQuery(
    private val appUserRepository: AppUserRepository,
    private val appUserProfileRepository: AppUserProfileRepository,
    private val dogRepository: DogRepository,
) {
    fun getAppUsersAndDogs(appUserId: Long): OwnerResponse {
        val user = appUserRepository.findById(appUserId).orElseThrow {
            throw IllegalArgumentException("App user $appUserId not found")
        }
        val dogs = dogRepository.findAllByAppUserId(appUserId)
        val profile = appUserProfileRepository.findByUserId(appUserId)

        return OwnerResponse.create(user, profile, dogs)
    }
}
