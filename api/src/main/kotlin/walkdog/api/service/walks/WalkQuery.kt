package walkdog.api.service.walks

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.model.dto.WalkStatResponse
import walkdog.api.domain.walks.repository.WalkRepository
import walkdog.api.domain.walks.repository.WalkStatRepository

@Service
@Transactional(readOnly = true)
class WalkQuery(
    private val walkRepository: WalkRepository,
    private val walkStatRepository: WalkStatRepository
) {
    fun findAllByAppUserId(appUserId: Long): List<WalkResponse> {
        try {
            val walks = walkRepository.findAllByAppUserId(appUserId)
            return walks.map { walk ->
                WalkResponse.create(walk)
            }
        } catch (ex: Exception) {
            println("ex.message = ${ex.message}")
        }
        return emptyList()
    }

    fun findWalk(appUserId: Long, walkId: Long): WalkResponse {
        val walk = walkRepository.findById(walkId)
        .orElseThrow {
            IllegalArgumentException("Could not find walk $walkId")
        }

        return WalkResponse.create(walk)
    }

    fun findUserWalkStats(appUserId: Long): WalkStatResponse {
        val stats = walkStatRepository.findByAppUserId(appUserId)
        return WalkStatResponse.create(stats)
    }
}
