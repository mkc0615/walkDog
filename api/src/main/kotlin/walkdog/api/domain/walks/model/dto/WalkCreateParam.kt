package walkdog.api.domain.walks.model.dto

data class WalkCreateParam (
    val walkId: Long,
    val dogIds: List<Long>,
    val title: String?,
    val description: String?,
    val startLongitude: Double,
    val startLatitude: Double
)
