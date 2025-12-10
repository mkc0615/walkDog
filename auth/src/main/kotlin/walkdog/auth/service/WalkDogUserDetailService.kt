package walkdog.auth.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.stereotype.Service
import walkdog.auth.domain.model.dto.WalkDogUserDetails
import walkdog.auth.domain.repository.AppUserRepository
import walkdog.auth.exception.AuthErrorType
import java.util.Collections

@Service
class WalkDogUserDetailService(
    private val appUserRepository: AppUserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = appUserRepository.findByUsername(username)
            ?: throw OAuth2AuthenticationException(
                OAuth2Error(
                    AuthErrorType.USER_NOT_FOUND.code,
                    AuthErrorType.USER_NOT_FOUND.message,
                    ""
                )
            )

        return WalkDogUserDetails(user, emptySet())
    }
}
