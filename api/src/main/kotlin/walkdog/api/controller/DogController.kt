package walkdog.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.domain.dogs.model.dto.DogCreateParam
import walkdog.api.domain.dogs.model.dto.DogResponse
import walkdog.api.service.dogs.DogCommand
import walkdog.api.service.dogs.DogQuery
import walkdog.api.service.users.AppUserQuery

@RestController
@RequestMapping("/api/v1/dogs")
class DogController(
    private val dogCommand: DogCommand,
    private val dogQuery: DogQuery
) {
    @GetMapping
    fun getOwnerDogs(
        @LoginUserContext userContext: LoginUserDetail
    ): List<DogResponse> {
        return dogQuery.getOwnerDogs(userContext.id)
    }

    @PostMapping
    fun addOwnerDog(@PathVariable id : Long, @RequestBody params: DogCreateParam) {
        dogCommand.create(id, params)
    }

}