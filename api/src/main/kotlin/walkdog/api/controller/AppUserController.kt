package walkdog.api.controller

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.appUsers.model.dto.OwnerParam
import walkdog.api.domain.appUsers.model.dto.OwnerResponse
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.service.dogs.DogCommand
import walkdog.api.service.users.AppUserCommand
import walkdog.api.service.users.AppUserQuery

@RestController
@RequestMapping("/api/v1/users")
class AppUserController(
    private val appUserQuery: AppUserQuery,
    private val appUserCommand: AppUserCommand,
    private val dogCommand: DogCommand,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody params: OwnerParam): OwnerParam {
        appUserCommand.create(params)
        return params;
    }

    @GetMapping("/me")
    fun getOwner(
        @LoginUserContext userContext: LoginUserDetail
    ): OwnerResponse {
        return appUserQuery.getAppUsersAndDogs(userContext.id)
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    fun updateOwner(
        @LoginUserContext userContext: LoginUserDetail,
        @RequestBody params: OwnerParam
    ) {
        appUserCommand.update(userContext.id, params)
    }
}
