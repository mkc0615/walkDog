package walkdog.api.domain.dogs.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import walkdog.api.domain.common.BaseEntity
import walkdog.api.domain.dogs.model.dto.DogCreateParam

@Entity
@Table(name = "dogs")
class Dog(
    @Column
    val appUserId: Long,
    @Column
    val name: String,
    @Column(name = "dog_type")
    @Enumerated(EnumType.STRING)
    val dogBreed: DogBreed,
    @Column
    val age: Int,
    @Column
    val weight: Long,
    @Column
    val gender: String,
    @Column
    val activity: Double,
    @Column
    val description: String,
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    companion object {
        fun create(appUserId: Long, params: DogCreateParam): Dog {
            return Dog(
                appUserId,
                params.name,
                dogBreed = DogBreed.fromCode(params.dogBreed) ?: throw IllegalArgumentException("Dog type not found"),
                params.age,
                params.weight,
                params.gender,
                params.activity,
                params.description
            )
        }
    }
}