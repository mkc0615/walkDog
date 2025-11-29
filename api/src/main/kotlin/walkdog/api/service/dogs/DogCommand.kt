package walkdog.api.service.dogs

import org.springframework.stereotype.Service
import walkdog.api.domain.dogs.model.Dog
import walkdog.api.domain.dogs.model.dto.DogCreateParam
import walkdog.api.domain.dogs.repository.DogRepository

@Service
class DogCommand(
    private val dogRepository: DogRepository,
) {
    fun create(appUserId: Long, params: DogCreateParam) {
        val dog = Dog.create(appUserId, params)
        dogRepository.save(dog)
    }
}
