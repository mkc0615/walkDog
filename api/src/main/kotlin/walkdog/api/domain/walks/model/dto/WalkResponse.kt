package walkdog.api.domain.walks.model.dto

import walkdog.api.domain.walks.model.Walk
import java.time.LocalDateTime

data class WalkResponse(
    val id: Long,
    val title: String,
    val distance: Double,
    val duration: Double,
    val description: String?,
    val startedAt: LocalDateTime?,
    val endedAt: LocalDateTime?,
    val dogIds: List<Long>,
) {
    companion object {
        fun create(walk: Walk): WalkResponse {
            return WalkResponse(
                id = walk.id,
                title = walk.title,
                description = walk.description,
                distance = walk.distance,
                duration = walk.duration,
                startedAt = walk.startedAt,
                endedAt = walk.endedAt,
                dogIds = listOf()
            )
        }
    }
}

data class WalkCreateResponse(
    val walkId: Long,
    val status: WalkStatus,
) {
    companion object {
        fun create(walkId: Long, status: WalkStatus): WalkCreateResponse {
            return WalkCreateResponse(
                walkId,
                status
            )
        }
    }
}
