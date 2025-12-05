package walkdog.api.domain.walks.model.dto

import jakarta.persistence.Column
import walkdog.api.domain.walks.model.WalkStat

class WalkStatResponse (
    val totalWalkCount: Int,
    val totalDistance: Double,
    val totalDuration: Double,
    val totalCalories: Double,
    val averageDistance: Double,
    val averageDuration: Double,
    val averageCalories: Double,
) {
    companion object {
        fun create(entity: WalkStat) : WalkStatResponse {
            return WalkStatResponse(
                totalWalkCount = entity.totalWalkCount,
                totalDistance = entity.totalDistance,
                totalDuration = entity.totalDuration,
                totalCalories = entity.totalCalories,
                averageDistance = entity.averageDistance,
                averageDuration = entity.averageDuration,
                averageCalories = entity.averageCalories
            )
        }
    }
}
