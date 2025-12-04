package walkdog.api.domain.walks.model.dto

import jakarta.persistence.Column

class WalkStatResponse (
    val totalWalks: Int,
    val totalDistance: Double,
    val totalDuration: Double,
    val totalCalories: Double,
    val averageDistance: Double,
    val averageDuration: Double,
    val averageCalories: Double,
)
