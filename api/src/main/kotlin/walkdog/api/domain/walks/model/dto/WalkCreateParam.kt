package walkdog.api.domain.walks.model.dto

import java.time.LocalDateTime

data class WalkCreateParam (
    val id: Long,
    val appUserId: String,
    val date: LocalDateTime,
    val title: String,
    val description: String?,
)