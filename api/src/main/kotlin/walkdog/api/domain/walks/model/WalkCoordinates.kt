package walkdog.api.domain.walks.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class WalkCoordinates(
    @Column
    private val walkId: Long,
    @Column
    private val latitude: Double,
    @Column
    private val longitude: Double,
    @Column
    private val sequenceIndex: Int
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    private val dateTime = LocalDateTime.now()
}