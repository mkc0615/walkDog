package walkdog.api.service.walks

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.walks.model.Walk
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkCreateResponse
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import walkdog.api.domain.walks.repository.WalkRepository

@Service
@Transactional
class WalkCommand(
    private val walkRepository: WalkRepository
) {
    fun createWalk(userId: Long, params: WalkCreateParam): WalkCreateResponse {
        val walk = Walk(userId, params)
        val result = walkRepository.save(walk)
        return WalkCreateResponse.create(result.id, result.status)
    }

    fun updateWalk(id: Long, params: WalkUpdateParam): WalkResponse {
        val walk = walkRepository.findById(id).orElse(null)
        if (walk == null) {
            throw IllegalArgumentException("cannot find walk")
        }
        walk.update(params)
        val result = walkRepository.save(walk)
        return WalkResponse.create(result)
    }

    fun startWalk(walkId: Long) {
        val walk = walkRepository.findById(walkId).orElse(null)
        if (walk == null) {
            throw IllegalArgumentException("cannot find walk")
        }
        walk.start()
    }

    fun finishWalk(walkId: Long) {
        val walk = walkRepository.findById(walkId).orElse(null)
        if (walk == null) {
            throw IllegalArgumentException("cannot find walk")
        }
        walk.finish()
    }

    fun removeWalk(id: Long) {
        walkRepository.deleteById(id)
    }
}