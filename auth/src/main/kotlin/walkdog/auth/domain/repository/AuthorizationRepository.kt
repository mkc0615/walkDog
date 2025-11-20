package walkdog.auth.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import walkdog.auth.domain.model.entity.Authorization
import java.util.Optional

@Repository
interface AuthorizationRepository: JpaRepository<Authorization, String> {
    fun findByAccessTokenValue(accessToken: String): Optional<Authorization>
}