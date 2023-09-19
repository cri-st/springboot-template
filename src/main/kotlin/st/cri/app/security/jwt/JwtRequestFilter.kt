package st.cri.app.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import st.cri.app.service.service_impl.JwtUserDetailsService
import java.io.IOException
import kotlin.String
import kotlin.Throws

@Component
class JwtRequestFilter(val jwtUserDetailsService: JwtUserDetailsService, val jwtTokenUtil: JwtTokenUtil) :
    OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestTokenHeader = request.getHeader("Authorization")
        val username: String?
        val jwtToken: String?
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7)
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken)
                //Once we get the token validate it.
                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails: UserDetails = jwtUserDetailsService.loadUserByUsername(username)
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        usernamePasswordAuthenticationToken.details =
                            WebAuthenticationDetailsSource().buildDetails(request)
                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                    }
                }
            } catch (e: MalformedJwtException) {
                logger.error("error en el jwt", e)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.")
                return
            } catch (e: ExpiredJwtException) {
                logger.error("error en el jwt", e)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is expired.")
                return
            } catch (e: UsernameNotFoundException) { // hay otras excepciones acá
                logger.error("error en el jwt", e)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The user does not exist.")
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}
