package walkdog.api.controller

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import walkdog.api.config.WalkDogPasswordEncoder
import walkdog.api.domain.appUsers.model.OwnerParam
import walkdog.api.domain.appUsers.model.OwnerResponse
import walkdog.api.domain.dogs.model.DogCreateParam
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

    @GetMapping("/test/{password}")
    fun testEncodePassword(@PathVariable password: String) {
        val encoded = passwordEncoder.encode(password)
        println("encoded  :::  $encoded")
    }
}
