package walkdog.api.domain.dogs.model

data class DogResponse(
    val dogId: Long,
    val dogName: String,
    val dogType: String,
    val age: Int,
    val description: String
) {
    companion object {
        fun create(dog: Dog): DogResponse {
            return DogResponse(
                dog.id,
                dog.name,
                dog.dogBreed.name,
                dog.age,
                dog.description
            )
        }
    }
}
