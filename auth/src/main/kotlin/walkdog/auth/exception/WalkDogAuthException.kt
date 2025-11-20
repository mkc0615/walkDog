package walkdog.auth.exception

open class WalkDogAuthException(
    baseError: BaseError
): RuntimeException() {
    override val message: String = baseError.message
    val code: String = baseError.code

}