package walkdog.api.domain.common

import walkdog.api.exception.ApiErrorType
import walkdog.api.exception.WalkDogException

class WalkDogPageable(
    page: Int? = 0,
    pageSize: Int? = 100,
    sort: List<String> = listOf("id"),
    val direction: Direction = Direction.DESC,
) {
    init {
        if (pageSize!! > 500 || pageSize < 1) throw WalkDogException(
            ApiErrorType.INVALID_PAGE_SIZE,
            "page sizes cannot be negative, zero, or over a 500"
        )
        if (page !! < 0) throw WalkDogException(
            ApiErrorType.INVALID_PAGE_SIZE,
            "page value should always be more than 0"
        )
    }

    enum class Direction {
        ASC, DESC
    }
}