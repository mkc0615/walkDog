package walkdog.api.domain.walks.model.dto

data class WalkResultParam(
    val dogIds: List<Long>,
    val duration: Double,
    val distance: Double
)