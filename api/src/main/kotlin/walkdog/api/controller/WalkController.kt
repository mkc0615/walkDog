package walkdog.api.controller

import org.springframework.web.bind.annotation.*
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.domain.walks.model.dto.WalkCreateParam
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
        val id = userContext.id
        return walkQuery.findAllByAppUserId(id)
    }

    @PostMapping
    fun createWalk(@RequestBody params: WalkCreateParam): WalkResponse {
        return walkCommand.createWalk(params)
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
        return walkCommand.stopWalk(id)
    }

    @DeleteMapping("{id}")
    fun removeWalk(@PathVariable id: Long) {
        return walkCommand.removeWalk(id)
    }
}
