package walkdog.api.domain.dogs.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.dogs.model.Dog

interface DogRepository: JpaRepository<Dog, Long> {
    fun findAllByAppUserId(appUserId: Long): List<Dog>
}
