package walkdog.api.domain.dogs.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "dog_types")
class DogType(
    @Column
    val name: String,

    @Column
    val avgWeight: Double,

    @Column
    val coldToleration: Int,

    @Column
    val hotToleration: Int,

    @Column
    val sensitivity: Int,

    @Column
    val energyLevel: Int,

    @Column
    val exerciseNeeds: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
