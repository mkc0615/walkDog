package walkdog.auth.domain.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "oauth2_client")
class Client {
    @Id
    var id: String? = null
    var clientId: String? = null
    var clientIdIssuedAt: Instant? = null
    var clientSecret: String? = null
    var clientSecretExpiresAt: Instant? = null
    var clientName: String? = null

    @Column(length = 1000)
    var clientAuthenticationMethods: String = ""

    @Column(length = 1000)
    var authorizationGrantTypes: String = ""

    @Column(length = 1000)
    var redirectUris: String = ""

    @Column(length = 1000)
    var postLogoutRedirectUris: String = ""

    @Column(length = 1000)
    var scopes: String? = null

    @Column(length = 1000)
    var clientSettings: String = ""

    @Column(length = 1000)
    var tokenSettings: String = ""
}
