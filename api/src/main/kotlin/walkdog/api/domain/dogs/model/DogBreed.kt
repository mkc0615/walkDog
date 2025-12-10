package walkdog.api.domain.dogs.model

enum class DogBreed(val code: String, val speedFactor: Double, val activityFactor: Double) {
    SMALL("below 10kg", 0.80, 3.0),
    MEDIUM("between 11kg and 25kg", 1.0, 3.5),
    LARGE("above 25kg", 1.2, 4.0);

    companion object {
        fun fromCode(code: String): DogBreed? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }
}
