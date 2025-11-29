package walkdog.api.domain.appUsers.model.dto

import walkdog.api.domain.appUsers.model.AppUser
import walkdog.api.domain.appUsers.model.AppUserProfile
import walkdog.api.domain.dogs.model.Dog
import walkdog.api.domain.dogs.model.dto.DogResponse

data class OwnerResponse(
    val userId: Long,
    val userName: String,
    val email: String,
    val myDogs: List<DogResponse>,
) {
    companion object {
        fun create(appUser: AppUser, profile: AppUserProfile, dogs: List<Dog>): OwnerResponse {
            return OwnerResponse(
                appUser.id,
                profile.name,
                appUser.email,
                myDogs = dogs.map { DogResponse.create(it) }
            )
        }
    }
}
