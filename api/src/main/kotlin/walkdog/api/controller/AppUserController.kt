package walkdog.api.controller

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import walkdog.api.domain.appUsers.model.dto.OwnerParam
import walkdog.api.domain.appUsers.model.dto.OwnerResponse
import walkdog.api.domain.dogs.model.dto.DogCreateParam
import walkdog.api.service.appUsers.AppUserCommand
import walkdog.api.service.appUsers.AppUserQuery
import walkdog.api.service.dogs.DogCommand

@RestController
@RequestMapping("/api/v1/users")
class AppUserController(
    private val appUserQuery: AppUserQuery,
    private val appUserCommand: AppUserCommand,
    private val dogCommand: DogCommand,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody params: OwnerParam): OwnerParam {
        appUserCommand.create(params)
        return params;
    }

    @GetMapping("/{id}")
    fun getOwner(@PathVariable id: Long): OwnerResponse {
        return appUserQuery.getAppUsersAndDogs(id)
    }

    @PostMapping("/{id}")
    fun updateOwner(@PathVariable id: String, @RequestBody params: OwnerParam) {
        appUserCommand.update(params)
    }

    @PostMapping("/{id}/dogs")
    fun addOwnerDog(@PathVariable id : Long, @RequestBody params: DogCreateParam) {
        dogCommand.create(id, params)
    }
}
