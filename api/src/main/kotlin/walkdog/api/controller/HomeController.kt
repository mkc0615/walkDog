package walkdog.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.service.users.AppUserQuery
import walkdog.api.service.walks.WalkQuery

@RestController
@RequestMapping("/api/v1/home")
class HomeController(
    private val userQuery: AppUserQuery,
    private val walkQuery: WalkQuery,
) {
    @GetMapping
    fun getHomeInfo(
        @LoginUserContext userContext: LoginUserDetail
    ) {
        val walkStats = walkQuery.findUserWalkStats(userContext.id)
    }
}