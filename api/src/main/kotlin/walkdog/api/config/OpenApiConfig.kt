package walkdog.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("WalkDog Auth API")
                    .version("v1.0.0")
                    .description("WalkDog Auth API")
            )
            .components(
                io.swagger.v3.oas.models.Components().addSecuritySchemes(
                    "basicAuth",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")
                )
            )
            .addSecurityItem(SecurityRequirement().addList("basicAuth"))
    }

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            val tokenPathItem = PathItem().post(
                Operation()
                    .tags(listOf("oauth2-controller"))
                    .operationId("token")
                    .summary("request access token")
                    .description("Login with Email")
                    .requestBody(
                        io.swagger.v3.oas.models.parameters.RequestBody()
                            .description("OAuth2 token request")
                            .content(
                                Content().addMediaType("application/x-www-form-urlencoded",
                                    MediaType().schema(
                                        Schema<Any>().type("object")
                                            .addProperty("grant_type",
                                                Schema<String>().nullable(false).type("string")
                                                    .apply { enum = listOf("password") }
                                            )
                                            .addProperty("username", Schema<String>().nullable(false).type("string").description("user email as id"))
                                            .addProperty("password", Schema<String>().nullable(false).type("string").description("password to login"))
                                    )
                                )
                            )
                    )
                    .responses(
                        ApiResponses()
                            .addApiResponse("200", ApiResponse()
                                .description("OK")
                                .content(
                                    Content().addMediaType(
                                        "application/json",
                                        MediaType().schema(
                                            Schema<Any>().type("object")
                                                .addProperty("access_token", Schema<String>().type("string"))
                                                .addProperty("token_type", Schema<String>().type("string"))
                                                .addProperty("scope", Schema<String>().type("string"))
                                                .addProperty("expires_in", Schema<Int>().type("integer").format("int32"))
                                        )
                                    )
                                )
                            )
                            .addApiResponse("400", ApiResponse()
                                .description("Bad Request")
                                .content(
                                    Content().addMediaType(
                                        "application/json",
                                        MediaType().schema(
                                            Schema<Any>().type("object")
                                                .addProperty("error", Schema<Any>().type("string"))
                                                .addProperty("error_description", Schema<Any>().type("string"))
                                        )
                                    )
                                )
                            )
                    )
            )

            openApi.paths.addPathItem("/oauth2/token", tokenPathItem)
        }
    }
}