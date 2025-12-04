package walkdog.api.controller

import org.springframework.web.bind.annotation.*
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkCreateResponse
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import walkdog.api.service.walks.WalkCommand
import walkdog.api.service.walks.WalkQuery

@RestController
@RequestMapping("/api/v1/walks")
class WalkController(
    private val walkQuery: WalkQuery,
    private val walkCommand: WalkCommand
) {
    @GetMapping
    fun getAllWalks(
        @LoginUserContext userContext: LoginUserDetail
    ): List<WalkResponse> {
        return walkQuery.findAllByAppUserId(userContext.id)
    }

    @PostMapping
    fun createWalk(
        @LoginUserContext userContext: LoginUserDetail,
        @RequestBody params: WalkCreateParam
    ): WalkCreateResponse {
        println("params = ${params.toString()}")
        return walkCommand.createWalk(userContext.id, params)
    }

    @PutMapping("/{id}")
    fun updateWalk(@PathVariable id: Long, @RequestBody params: WalkUpdateParam): WalkResponse {
        return walkCommand.updateWalk(id, params)
    }

    @PatchMapping("{id}/start")
    fun startWalk(@PathVariable id: Long) {
        return walkCommand.startWalk(id)
    }

    @PatchMapping("{id}/stop")
    fun stopWalk(@PathVariable id: Long) {
        return walkCommand.finishWalk(id)
    }

    @DeleteMapping("{id}")
    fun removeWalk(@PathVariable id: Long) {
        return walkCommand.removeWalk(id)
    }
}
