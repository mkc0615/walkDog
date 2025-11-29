package walkdog.api.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import walkdog.api.annotation.LoginUserContext
import walkdog.api.domain.appUsers.AppUserRepository
import walkdog.api.domain.appUsers.model.AppUser
import walkdog.api.domain.common.LoginUserDetail
import walkdog.api.exception.NotAvailableUserException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException
import java.util.Locale

@Component
class LoginUserContextArgumentResolver(
    private val userRepository: AppUserRepository
): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUserContext::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): LoginUserDetail? {
        when (webRequest.userPrincipal) {
            is JwtAuthenticationToken -> {
                val authentication = webRequest.userPrincipal as JwtAuthenticationToken
                val jwt: Jwt = authentication.token
                val username = jwt.claims["sub"] as String
                val user = userRepository.findByEmail(username).validUser()
                val ip = webRequest.getNativeRequest(HttpServletRequest::class.java)
                    ?.let { getUserNetIp(it) }
                    ?: "IP UNKNOWN"
                val mac = getUserMacAddress()
                return user?.let {
                    LoginUserDetail.create(it.id, it.email, ip, mac)
                }
            }

            is UsernamePasswordAuthenticationToken -> {
                val authentication = webRequest.userPrincipal as UsernamePasswordAuthenticationToken
                val user = userRepository.findByEmail(authentication.name).validUser()
                val ip = webRequest.getNativeRequest(HttpServletRequest::class.java)
                    ?.let { getUserNetIp(it) }
                    ?: "IP UNKNOWN"
                val mac = getUserMacAddress()
                return user?.let {
                    LoginUserDetail.create(it.id, it.email, ip, mac)
                }
            }

            else -> throw NotAvailableUserException()
        }
    }

    fun getUserNetIp(request: HttpServletRequest): String {
        val xForwardedForHeader = request.getHeader("X-Forwarded-For")
        return if (xForwardedForHeader == null) {
            request.remoteAddr
        } else {
            xForwardedForHeader.split(",")[0].trim()
        }
    }

    fun getUserMacAddress(): String {
        var macAddress = ""
        try {
            val ip = InetAddress.getLocalHost()
            val network = NetworkInterface.getByInetAddress(ip)
            val mac = network.hardwareAddress
            val sb = StringBuilder()

            for (i in mac.indices) {
                sb.append(String.format("%02X%s", mac[i], if ((i < mac.size - 1)) "-" else ""))
            }
            macAddress = sb.toString().lowercase(Locale.getDefault())
        } catch (ignore: Exception) {
            when(ignore) {
                is UnknownHostException, is SocketException, is NullPointerException -> {
                    // TODO: set with logger
                    println("error occurred for mac address checking")
                }
            }
        }
        return macAddress
    }

    private fun AppUser?.validUser(): AppUser? {
        this?.let {
            if(it.isDeleted == "Y") throw NotAvailableUserException()
        }
        return this
    }
}