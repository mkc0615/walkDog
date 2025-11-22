package walkdog.api.domain.walks.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import walkdog.api.domain.common.BaseEntity
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
@Table(name = "walks")
class Walk(
    params: WalkCreateParam
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val appUserId: Long = params.appUserId

    @Column(length = 255)
    var title: String = params.title

    @Column(nullable = false)
    val date: LocalDateTime = params.date

    @Column
    var distance: Double = 0.0

    @Column(length = 255)
    var description: String = params.description ?: ""

    @Column
    var startedAt: LocalDateTime? = null

    @Column
    var endedAt: LocalDateTime? = null

    @Column
    var duration: Long = 0

    fun update(params: WalkUpdateParam): Walk {
        this.title = params.title ?: this.title
        this.description = params.description ?: this.description
        return this
    }

    fun start() {
        this.startedAt = LocalDateTime.now()
    }

    fun end() {
        val startTime = startedAt ?: LocalDateTime.now()
        val current = LocalDateTime.now()
        this.endedAt = current

        val zone = ZoneId.systemDefault()
        val newDuration = Duration.between(startTime.atZone(zone), current.atZone(zone)).toMillis()

        this.duration = newDuration
    }
}