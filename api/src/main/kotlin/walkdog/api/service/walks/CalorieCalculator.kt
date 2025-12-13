package walkdog.api.service.walks

import org.springframework.stereotype.Service
import walkdog.api.domain.dogs.model.DogBreed
import walkdog.api.domain.dogs.repository.DogTypeRepository

@Service
class CalorieCalculator(
    dogTypeRepository: DogTypeRepository
) {
    fun calculateOwnerCalories(duration: Double, distance: Double): Double {
        // calories = MET value x weight in kg x distance in km
        val weight = 80.0
        val metValue = getMetValueOfOwner(duration, distance)
        return metValue * weight * distance
    }

    fun calculateDogCalories(dogBreed: DogBreed, duration: Double, weight: Long): Double {
        // dog calories(kcal) = weight in kg x duration in hours x activity factor x speed factor
        val speedFactor = dogBreed.speedFactor
        val activityFactor = dogBreed.activityFactor
        return weight * duration * activityFactor * speedFactor
    }

    private fun getMetValueOfOwner(duration: Double, distance: Double): Double {
        val speed = distance / duration
        return if (speed < 3.2) {
            2.5
        } else if (speed < 4.8) {
            3.3
        } else if (speed < 5.6) {
            4.3
        } else {
            5.0
        }
    }
}