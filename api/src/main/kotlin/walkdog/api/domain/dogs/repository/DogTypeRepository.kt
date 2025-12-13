package walkdog.api.domain.dogs.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.dogs.model.DogType

interface DogTypeRepository: JpaRepository<DogType, Long> {
}