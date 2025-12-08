package walkdog.api.service.walks

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.walks.model.Walk
import walkdog.api.domain.walks.model.WalkCoordinates
import walkdog.api.domain.walks.model.WalkDog
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkCreateResponse
import walkdog.api.domain.walks.model.dto.WalkPositionParam
import walkdog.api.domain.walks.model.dto.WalkResultParam
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import walkdog.api.domain.walks.repository.WalkDogRepository
import walkdog.api.domain.walks.repository.WalkRepository
import walkdog.api.domain.walks.repository.WalkStatRepository
import walkdog.api.domain.walks.repository.WalkTrackRepository

@Service
@Transactional
class WalkCommand(
    private val walkRepository: WalkRepository,
    private val walkStatRepository: WalkStatRepository,
    private val walkTrackRepository: WalkTrackRepository,
    private val walkDogRepository: WalkDogRepository,
) {
    fun createWalk(userId: Long, params: WalkCreateParam): WalkCreateResponse {
        val walk = Walk(userId, params)
        walk.start()
        val walkSaved = walkRepository.save(walk)
        val dogIds = params.dogIds

        mappingWalkDog(walk.id, dogIds)

        return WalkCreateResponse.create(walkSaved.id, walkSaved.status)
    }

    fun updateCoordinates(walkId: Long, coordinates: List<WalkPositionParam>) {
        val tracks = coordinates.map { track ->
            WalkCoordinates(walkId, track)
        }
        walkTrackRepository.saveAll(tracks)
    }

    fun finishWalk(appUserId: Long, walkId: Long, params: WalkResultParam) {
        val walk = walkRepository.findById(walkId).orElse(null)
        if (walk == null) {
            throw IllegalArgumentException("cannot find walk")
        }
        walk.finish(params.duration, params.distance)

        updateWalkStats(appUserId, params)
    }

    private fun mappingWalkDog(walkId: Long, dogIds: List<Long>) {
        val walkDogs: List<WalkDog> = dogIds.map { dogId ->
            WalkDog(dogId, walkId)
        }
        walkDogRepository.saveAll(walkDogs)
    }

    private fun updateWalkStats(appUserId: Long, params: WalkResultParam) {
        val calories =  calculateCalories(params)

        val dogCalories = params.dogIds.map { dogId ->
            calculateDogCalories(dogId)
        }

        val stats = walkStatRepository.findByAppUserId(appUserId)
        stats.updateCount()
        stats.updateTotals(params.duration, params.distance, calories)
        stats.updateAverages()
    }

    private fun calculateCalories(params: WalkResultParam): Double {
        // TODO: calculate calories burned
        return 0.0
    }

    private fun calculateDogCalories(dogId: Long): Double {
        // TODO: calculate dog calories burned
        return 0.0
    }

    fun updateDetails(walkId: Long, params: WalkUpdateParam) {
        val walk = walkRepository.findById(walkId).orElseThrow()
            ?: throw IllegalArgumentException("cannot find walk")

        walk.update(params)
    }

    fun removeWalk(id: Long) {
        walkRepository.deleteById(id)
    }
}
