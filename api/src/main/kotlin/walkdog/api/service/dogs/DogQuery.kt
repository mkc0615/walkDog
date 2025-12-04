package walkdog.api.service.dogs

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import walkdog.api.domain.dogs.model.dto.DogResponse
import walkdog.api.domain.dogs.repository.DogRepository
import walkdog.api.domain.walks.model.dto.WalkResponse

@Service
@Transactional(readOnly = true)
class DogQuery(
    private val dogRepository: DogRepository
) {

    fun getOwnerDogs(id: Long): List<DogResponse> {
        val dogs = dogRepository.findAllByAppUserId(id);
        return dogs.map { dog ->
            DogResponse.create(dog)
        }
    }
}