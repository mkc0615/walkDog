package walkdog.auth.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import walkdog.auth.domain.model.entity.Client
import java.util.*

interface ClientRepository: JpaRepository<Client, String> {
    fun findByClientId(clientId: String): Optional<Client>
}
