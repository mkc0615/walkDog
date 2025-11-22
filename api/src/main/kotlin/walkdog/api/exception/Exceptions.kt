package walkdog.api.exception

import org.springframework.http.HttpStatus

enum class ApiErrorType(
    override val code: String,
    override val message: String,
    override val httpStatus: HttpStatus
): WalkDogError {




}