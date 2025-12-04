package walkdog.api.domain.walks.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import walkdog.api.domain.common.BaseEntity

@Entity
@Table(name = "walk_stats")
class WalkStat(

    @Column
    val appUserId : Long
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id = 0

    @Column
    var totalWalkCount : Int = 0

    @Column
    var totalDistance : Double = 0.0

    @Column
    var totalDuration : Double = 0.0

    @Column
    var totalCalories : Double = 0.0

    @Column
    var averageDistance : Double = 0.0

    @Column
    var averageDuration : Double = 0.0

    @Column
    var averageCalories : Double = 0.0

    fun updateCount() {
        totalWalkCount++
    }

    fun updateTotals() {

    }

    fun updateAverages() {

    }
}