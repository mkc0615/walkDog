package walkdog.api.domain.walks.model.dto

data class WalkUpdateParam(
    val title: String?,
    val description: String?,
)

data class WalkPositionParam(
    val longitude: Double,
    val latitude: Double,
)