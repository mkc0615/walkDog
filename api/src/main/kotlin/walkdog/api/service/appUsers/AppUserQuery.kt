package walkdog.api.service.appUsers

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.appUsers.AppUserRepository
import walkdog.api.domain.appUsers.model.OwnerResponse
import walkdog.api.domain.dogs.repository.DogRepository

@Service
@Transactional
class AppUserQuery(
    private val appUserRepository: AppUserRepository,
    private val dogRepository: DogRepository,
) {
    fun getAppUsersAndDogs(appUserId: Long): OwnerResponse {
        val user = appUserRepository.findById(appUserId).orElseThrow {
            throw IllegalArgumentException("App user $appUserId not found")
        }
        val dogs = dogRepository.findAllByAppUserId(appUserId)

        return OwnerResponse.create(user, dogs)
    }
}