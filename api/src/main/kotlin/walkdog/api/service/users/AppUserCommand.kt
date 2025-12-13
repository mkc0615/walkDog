package walkdog.api.service.users

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import walkdog.api.domain.appUsers.AppUserRepository
import walkdog.api.domain.appUsers.model.AppUser
import walkdog.api.domain.appUsers.model.dto.AppUserParam
import walkdog.api.domain.walks.model.WalkStat
import walkdog.api.domain.walks.repository.WalkStatRepository

@Service
@Transactional
class AppUserCommand(
    private val appUserRepository: AppUserRepository,
    private val walkStatRepository: WalkStatRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(params: AppUserParam) {
        val encodedPassword = passwordEncoder.encode(params.password)
        params.password = encodedPassword

        val appUser = AppUser.create(params)
        appUserRepository.save(appUser)

        val walkStats = WalkStat(appUser.id)
        walkStatRepository.save(walkStats)
    }

    fun update(appUserId: Long, params: AppUserParam) {
        // TODO: update user info
    }
}
