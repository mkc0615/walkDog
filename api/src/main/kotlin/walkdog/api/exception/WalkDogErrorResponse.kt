package walkdog.api.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "API error response")
data class WalkDogErrorResponse(
    @Schema(description = "error code")
    val code: String,
    @Schema(description = "error message")
    val message: String,
    @Schema(description = "error detail list")
    val details: List<Any> = emptyList()
) {
    constructor(ex: WalkDogException): this(
        code = ex.code,
        message = ex.message,
        details = ex.details
    )
}
