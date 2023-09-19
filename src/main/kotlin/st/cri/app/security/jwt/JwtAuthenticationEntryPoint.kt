package st.cri.app.security.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.Serializable
import javax.servlet.http.HttpServletResponse


/***
 * Clase que rechaza requests no autenticadas y envia un 401
 */
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {
    @Throws(IOException::class)
    override fun commence(
        request: jakarta.servlet.http.HttpServletRequest?,
        response: jakarta.servlet.http.HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}