package walkdog.auth.domain.model.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import walkdog.auth.domain.model.entity.AppUser
import java.util.Collections

class WalkDogUserDetails(
    private val user: AppUser,
    private val allowGroups: Set<String>,
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return Collections.emptyList()
    }

    override fun getPassword(): String? {
        return user.password
    }

    override fun getUsername(): String? {
        return user.email
    }

}
