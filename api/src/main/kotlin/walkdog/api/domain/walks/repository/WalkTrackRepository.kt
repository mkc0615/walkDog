package walkdog.api.domain.walks.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.walks.model.WalkCoordinates

interface WalkTrackRepository: JpaRepository<WalkCoordinates, Long> {

}