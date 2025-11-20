package walkdog.api.controller

import org.springframework.web.bind.annotation.*
import walkdog.api.domain.walks.model.dto.WalkCreateParam
import walkdog.api.domain.walks.model.dto.WalkResponse
import walkdog.api.domain.walks.model.dto.WalkUpdateParam
import walkdog.api.service.WalkCommand
import walkdog.api.service.WalkQuery

@RestController
@RequestMapping("/api/v1/walks")
class WalkController(
    private val walkQuery: WalkQuery,
    private val walkCommand: WalkCommand
) {

    @GetMapping
    fun getAllWalks(): List<WalkResponse> {
        val userId = ""
        return walkQuery.findAllByAppUserId(userId)
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

    @PatchMapping("{id}/end")
    fun endWalk(@PathVariable id: Long) {
        return walkCommand.endWalk(id)
    }

    @DeleteMapping("{id}")
    fun removeWalk(@PathVariable id: Long) {
        return walkCommand.removeWalk(id)
    }
}
