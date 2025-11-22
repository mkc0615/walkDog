package walkdog.api.exception

import org.springframework.http.HttpStatus

interface WalkDogError {
    val code: String
    val message: String
    val httpStatus: HttpStatus
}