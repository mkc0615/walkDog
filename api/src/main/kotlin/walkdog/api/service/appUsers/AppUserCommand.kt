package walkdog.api.service.appUsers

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import walkdog.api.domain.appUsers.AppUserRepository
import walkdog.api.domain.appUsers.model.AppUser
import walkdog.api.domain.appUsers.model.OwnerParam
import walkdog.api.domain.appUsers.model.OwnerResponse

@Service
@Transactional
class AppUserCommand(
    private val appUserRepository: AppUserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(params: OwnerParam) {
        val encodedPassword = passwordEncoder.encode(params.password)
        params.password = encodedPassword

        val appUser = AppUser.create(params)
        appUserRepository.save(appUser)
    }

    fun update(params: OwnerParam) {
        val appUser = AppUser.create(params)
        appUser.update(params)
        appUserRepository.save(appUser)
    }
}
