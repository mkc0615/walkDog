package walkdog.api.domain.walks.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.walks.model.Walk

interface WalkRepository : JpaRepository<Walk, Long> {
    fun findAllByAppUserId(appUserId: Long): List<Walk>
}
