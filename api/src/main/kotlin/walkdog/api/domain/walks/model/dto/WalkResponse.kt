package walkdog.api.domain.walks.model.dto

import walkdog.api.domain.walks.model.Walk
import java.time.LocalDateTime

data class WalkResponse(
    val id: Long,
    val title: String,
    val date: LocalDateTime,
    val distance: Double,
    val duration: Long,
) {
    companion object {
        fun create(walk: Walk): WalkResponse {
            return WalkResponse(
                id = walk.id,
                title = walk.title,
                date = walk.date,
                distance = walk.distance,
                duration = walk.duration
            )
        }
    }
}