package walkdog.api.domain.walks.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.api.domain.walks.model.WalkStat
import walkdog.api.domain.walks.model.dto.WalkResponse

interface WalkStatRepository: JpaRepository<WalkStat, Long> {
    fun findByAppUserId(appUserId: Long): WalkStat
}