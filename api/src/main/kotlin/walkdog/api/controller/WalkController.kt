package walkdog.api.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkCreateResponse
import walkdog.api.domain.walks.model.dto.WalkPositionParam
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.model.dto.WalkResultParam
import walkdog.api.domain.walks.model.dto.WalkTrackParam
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

    @GetMapping("/{walkId}")
    fun getWalk(
        @LoginUserContext userContext: LoginUserDetail,
        @PathVariable walkId: Long
    ): WalkResponse {
        return walkQuery.findWalk(userContext.id, walkId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createWalk(
        @LoginUserContext userContext: LoginUserDetail,
        @RequestBody params: WalkCreateParam
    ): WalkCreateResponse {
        return walkCommand.createWalk(userContext.id, params)
    }

    @PostMapping("/{walkId}/track")
    fun trackWalk(
        @LoginUserContext userContext: LoginUserDetail,
        @PathVariable("walkId") walkId: Long,
        @RequestBody params: WalkTrackParam
    ) {
        walkCommand.updateCoordinates(walkId, params.coordinates)
    }

    @PostMapping("{walkId}/stop")
    @ResponseStatus(HttpStatus.OK)
    fun stopWalk(
        @LoginUserContext userContext: LoginUserDetail,
        @PathVariable walkId: Long,
        @RequestBody params: WalkResultParam
    ) {
        return walkCommand.finishWalk(userContext.id, walkId, params)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeWalk(@PathVariable id: Long) {
        return walkCommand.removeWalk(id)
    }
}
