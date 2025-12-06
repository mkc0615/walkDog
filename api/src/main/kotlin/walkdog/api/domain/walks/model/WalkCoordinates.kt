package walkdog.api.domain.walks.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import walkdog.api.domain.walks.model.dto.WalkPositionParam
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "walk_tracks")
class WalkCoordinates(
    @Column
    private val walkId: Long,
    private val params: WalkPositionParam
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    private val latitude: Double = params.latitude

    @Column
    private val longitude: Double = params.longitude

    @Column(nullable = false)
    val timestamp: Instant = params.timestamp

    @Column(nullable = true)
    val accuracy: Double = params.accuracy

    @Column
    private val createdAt = LocalDateTime.now()
}