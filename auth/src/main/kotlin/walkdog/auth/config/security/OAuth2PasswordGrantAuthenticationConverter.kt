package walkdog.auth.config.security

import jakarta.annotation.Nullable
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class OAuth2PasswordGrantAuthenticationConverter: AuthenticationConverter {
    @Nullable
    override fun convert(request: HttpServletRequest): OAuth2PasswordGrantAuthenticationToken? {

        val grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE)
        if (PASSWORD_GRANT_TYPE.value != grantType) {
            return null
        }

        val clientPrincipal = SecurityContextHolder.getContext().authentication
        val parameters = getParameters(request)

        val scope = parameters.getFirst(OAuth2ParameterNames.SCOPE)
        val scopes = if (scope != null) mutableSetOf(
            *scope.split(" ".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        ) else null

        return OAuth2PasswordGrantAuthenticationToken(
            parameters.getFirst(OAuth2ParameterNames.USERNAME)!!,
            parameters.getFirst(OAuth2ParameterNames.PASSWORD)!!,
            clientPrincipal,
            scopes
        )
    }

    companion object {
        val PASSWORD_GRANT_TYPE: AuthorizationGrantType = AuthorizationGrantType("password")
        private fun getParameters(request: HttpServletRequest): MultiValueMap<String, String> {
            val parameterMap = request.parameterMap
            val parameters: MultiValueMap<String, String> = LinkedMultiValueMap(parameterMap.size)
            parameterMap.forEach { (key: String, values: Array<String?>) ->
                if (values.isNotEmpty()) {
                    for(value in values) {
                        parameters.add(key, value)
                    }
                }
            }
            return parameters
        }
    }
}
