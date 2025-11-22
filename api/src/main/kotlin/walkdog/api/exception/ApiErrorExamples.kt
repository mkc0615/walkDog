package walkdog.api.exception

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorExample(
    val value: ApiErrorType
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorExamples(
    val value: Array<ApiErrorType>
)
