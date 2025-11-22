package walkdog.api.service.walks

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.repository.WalkRepository

@Service
@Transactional
class WalkQuery(
    private val walkRepository: WalkRepository
) {
    fun findAllByAppUserId(appUserId: Long): List<WalkResponse> {
        val walks = walkRepository.findAllByAppUserId(appUserId)
        return walks.map { walk ->
            WalkResponse.create(walk)
        }
    }
}
