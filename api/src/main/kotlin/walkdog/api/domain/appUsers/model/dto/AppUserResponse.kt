package walkdog.api.domain.appUsers.model.dto

import walkdog.api.domain.appUsers.model.AppUser
import walkdog.api.domain.appUsers.model.AppUserProfile

data class AppUserResponse(
    val userId: Long,
    val username: String,
    val email: String,
    val imageUrl: String? = null,
) {
    companion object {
        fun create(appUser: AppUser, profile: AppUserProfile): AppUserResponse {
            return AppUserResponse(
                appUser.id,
                appUser.username,
                profile.email,
                imageUrl = profile.imageUrl,
            )
        }
    }
}
