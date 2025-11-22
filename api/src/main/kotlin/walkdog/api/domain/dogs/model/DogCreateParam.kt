package walkdog.api.domain.dogs.model

data class DogCreateParam(
    val name: String,
    val dogBreed: String,
    val age: Int,
    val weight: Long,
    val gender: String,
    val description: String,
)
