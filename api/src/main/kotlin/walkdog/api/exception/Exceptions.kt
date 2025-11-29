package walkdog.api.exception

import org.springframework.http.HttpStatus

enum class ApiErrorType(
    override val code: String,
    override val message: String,
    override val httpStatus: HttpStatus
): WalkDogError {

    INVALID_PAGE_SIZE("INVALID_PAGE_SIZE", "Page size is not allowed", HttpStatus.BAD_REQUEST),
    NOT_AVAILABLE_USER("NOT_AVAILABLE_USER", "User is not available", HttpStatus.BAD_REQUEST),

}

class NotAvailableUserException: WalkDogException(ApiErrorType.NOT_AVAILABLE_USER, "")