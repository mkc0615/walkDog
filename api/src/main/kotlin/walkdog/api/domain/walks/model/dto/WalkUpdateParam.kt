package walkdog.api.domain.walks.model.dto

import java.time.Instant

data class WalkUpdateParam(
    val title: String?,
    val description: String?,
)

data class WalkTrackParam(
    val coordinates: List<WalkPositionParam>,
)

data class WalkPositionParam(
    val longitude: Double,
    val latitude: Double,
    val timestamp: Instant,
    val accuracy: Double
)