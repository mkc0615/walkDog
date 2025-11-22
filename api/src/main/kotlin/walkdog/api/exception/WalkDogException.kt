package walkdog.api.exception

class WalkDogException(
    commonError: WalkDogError,
    additionalMessage: String = "",
    val details: List<String> = emptyList()
): RuntimeException() {
    val code: String = commonError.code
    override val message: String = "${commonError.message} $additionalMessage"
}
