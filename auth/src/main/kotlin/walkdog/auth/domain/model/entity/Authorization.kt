package walkdog.auth.domain.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name="oauth2_authorization")
class Authorization {
    @Id
    @Column
    var id: String? = null
    var registeredClientId: String? = null
    var principalName: String? = null
    var authorizationGrantType: String = ""

    @Column(length = 1000)
    var authorizedScopes: String? = null

    @Column(length = 4000)
    var attributes: String = ""

    @Column(length = 500)
    var state: String? = null

    @Column(length = 4000)
    var authorizationCodeValue: String? = null
    var authorizationCodeIssuedAt: Instant? = null
    var authorizationCodeExpiresAt: Instant? = null
    var authorizationCodeMetadata: String = ""

    @Column(length = 4000)
    var accessTokenValue: String? = null
    var accessTokenIssuedAt: Instant? = null
    var accessTokenExpiresAt: Instant? = null

    @Column(length = 2000)
    var accessTokenMetadata: String = ""
    var accessTokenType: String? = null

    @Column(length = 1000)
    var accessTokenScopes: String? = null

    @Column(length = 4000)
    var refreshTokenValue: String? = null
    var refreshTokenIssuedAt: Instant? = null
    var refreshTokenExpiresAt: Instant? = null

    @Column(length = 2000)
    var refreshTokenMetadata: String? = null
}