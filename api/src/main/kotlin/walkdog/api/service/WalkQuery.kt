package walkdog.api.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.repository.WalkRepository
import java.time.format.DateTimeFormatter

@Service
@Transactional
class WalkQuery(
    private val walkRepository: WalkRepository
) {
    fun findAllByAppUserId(appUserId: String): List<WalkResponse> {
        val walks = walkRepository.findAllByAppUserId(appUserId)
        return walks.map { walk ->
            WalkResponse.create(walk)
        }
    }
}
