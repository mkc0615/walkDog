package walkdog.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import walkdog.api.exception.ApiErrorExample
import walkdog.api.exception.ApiErrorExamples
import walkdog.api.exception.ApiErrorType
import walkdog.api.exception.WalkDogErrorResponse

@Configuration
class OpenApiConfig {
    @Value("\${walkdog.oauth.token-uri}")
    private lateinit var tokenUri: String

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .components(createSecurityScheme())
            .info(createApiInfo())
            .addSecurityItem(SecurityRequirement().addList("oauth2"))
    }

    private fun createSecurityScheme(): Components {
        val securitySchemes = SecurityScheme()
            .type(SecurityScheme.Type.OAUTH2)
            .flows(
                OAuthFlows().password(OAuthFlow().tokenUrl(tokenUri))
            )
        return Components().addSecuritySchemes("oauth2", securitySchemes)
    }

    private fun createApiInfo(): Info {
        return Info()
            .apply {
                title = "Walkdog API"
                version = "1.0.0"
                description = "Walkdog Server API"
            }
    }

    @Bean
    fun customize(): OperationCustomizer = OperationCustomizer { operation, handlerMethod ->
        handlerMethod.getMethodAnnotation(ApiErrorExamples::class.java)?.let {
            operation.addErrorResponses(it.value)
        } ?: handlerMethod.getMethodAnnotation(ApiErrorExample::class.java)?.let {
            operation.addErrorResponse(it.value)
        }
        operation
    }

    private fun Operation.addErrorResponses(errors: Array<ApiErrorType>) {
        errors.groupBy {
            it.httpStatus.value().toString()
        }.forEach { (status, errors) ->
            responses.addApiResponse(status, ApiResponse().content(createContent(errors)))
        }
    }

    private fun Operation.addErrorResponse(error: ApiErrorType) {
        responses.addApiResponse(
            error.httpStatus.value().toString(),
            ApiResponse().content(createContent(listOf(error)))
        )
    }

    private fun createContent(errors: List<ApiErrorType>): Content {
        val mediaType = MediaType().apply {
            errors.forEach { error ->
                addExamples(error.name, createExample(error))
            }
        }
        return Content().addMediaType("application/json", mediaType)
    }

    private fun createExample(error: ApiErrorType): Example {
        return Example().apply {
            value = WalkDogErrorResponse(
                code = error.code,
                message = error.message,
            )
        }
    }
}