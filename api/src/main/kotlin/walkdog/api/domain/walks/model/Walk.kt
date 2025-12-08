package walkdog.api.domain.walks.model

import jakarta.persistence.*
import walkdog.api.domain.common.BaseEntity
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkStatus
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import java.time.LocalDateTime

@Entity
@Table(name = "walks")
class Walk(
    @Column(nullable = false)
    val appUserId: Long,

    params: WalkCreateParam
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(length = 255)
    var title: String = params.title ?: "Walk Of The Day"

    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now()

    @Column
    var distance: Double = 0.00

    @Column(length = 255)
    var description: String = params.description ?: ""

    @Column(name = "walk_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: WalkStatus = WalkStatus.STARTED

    @Column
    var startedAt: LocalDateTime? = null

    @Column
    var endedAt: LocalDateTime? = null

    @Column
    var duration: Double = 0.0

    @Column
    var caloriesBurned: Double = 0.0

    @Column
    val startLongitude: Double = params.startLongitude

    @Column
    val startLatitude: Double = params.startLatitude

    fun update(params: WalkUpdateParam) {
        this.title = params.title ?: this.title
        this.description = params.description ?: this.description
    }

    fun start() {
        this.status = WalkStatus.STARTED
        this.startedAt = LocalDateTime.now()
    }

    fun finish(duration: Double, distance: Double) {
        this.duration = duration
        this.distance = distance
        this.status = WalkStatus.FINISHED
        this.endedAt = LocalDateTime.now()
    }
}