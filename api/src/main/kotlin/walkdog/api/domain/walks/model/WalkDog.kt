package walkdog.api.domain.walks.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class WalkDog(
    @Column
    private val dogId: Long,
    @Column
    private val walkId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    val caloriesBurned: Long = 0L
}
