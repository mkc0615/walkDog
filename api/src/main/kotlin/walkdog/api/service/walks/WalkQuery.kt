package walkdog.api.service.walks

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.model.dto.WalkStatResponse
import walkdog.api.domain.walks.repository.WalkRepository
import java.lang.System.console

@Service
@Transactional(readOnly = true)
class WalkQuery(
    private val walkRepository: WalkRepository
) {
    fun findAllByAppUserId(appUserId: Long): List<WalkResponse> {
        println("appUserId = $appUserId")
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

    fun findUserWalkStats(appUserId: Long): WalkStatResponse {
        return WalkStatResponse(
            0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
        )
    }
}
