package walkdog.api.domain.dogs.model.dto

data class DogCreateParam(
    val name: String,
    val dogBreed: String,
    val age: Int,
    val weight: Long,
    val gender: String,
    val activity: Double,
    val description: String,
)
