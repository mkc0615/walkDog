package walkdog.api.service.walks

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.walks.model.Walk
import walkdog.api.domain.walks.model.WalkCoordinates
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkCreateResponse
import walkdog.api.domain.walks.model.dto.WalkPositionParam
import walkdog.api.domain.walks.model.dto.WalkResultParam
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import walkdog.api.domain.walks.repository.WalkRepository
import walkdog.api.domain.walks.repository.WalkStatRepository
import walkdog.api.domain.walks.repository.WalkTrackRepository

@Service
@Transactional
class WalkCommand(
    private val walkRepository: WalkRepository,
    private val walkStatRepository: WalkStatRepository,
    private val walkTrackRepository: WalkTrackRepository
) {
    fun createWalk(userId: Long, params: WalkCreateParam): WalkCreateResponse {
        val walk = Walk(userId, params)
        val result = walkRepository.save(walk)
        // map dogs
        val dogIds = params.dogIds.toMutableSet()


        // return response
        return WalkCreateResponse.create(result.id, result.status)
    }

    fun updateCoordinates(walkId: Long, params: WalkPositionParam) {
        val track = WalkCoordinates(walkId, params.latitude, params.longitude)
        walkTrackRepository.save(track)
    }

    fun finishWalk(appUserId: Long, walkId: Long, params: WalkResultParam) {
        val walk = walkRepository.findById(walkId).orElse(null)
        if (walk == null) {
            throw IllegalArgumentException("cannot find walk")
        }
        walk.finish(params.duration, params.distance)

        // TODO: calculate calories for owner and dogs
        val dogIds: List<Long> = params.dogIds

        // TODO: calculate calories burned
        val calories = 0.0
        val stats = walkStatRepository.findByAppUserId(appUserId)
        stats.updateCount()
        stats.updateTotals(params.duration, params.distance, calories)
        stats.updateAverages()
    }

    fun removeWalk(id: Long) {
        walkRepository.deleteById(id)
    }
}
