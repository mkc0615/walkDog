package walkdog.api.domain.appUsers.model

import walkdog.api.domain.dogs.model.Dog
import walkdog.api.domain.dogs.model.DogResponse

data class OwnerResponse(
    val userId: Long,
    val userName: String,
    val email: String,
    val myDogs: List<DogResponse>,
) {
    companion object {
        fun create(appUser: AppUser, dogs: List<Dog>): OwnerResponse {
            return OwnerResponse(
                appUser.id,
                appUser.name,
                appUser.email,
                myDogs = dogs.map { DogResponse.create(it) }
            )
        }
    }
}
