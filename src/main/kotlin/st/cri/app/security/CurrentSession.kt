package st.cri.app.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import st.cri.app.dao.UserDao
import st.cri.app.domain.user.User

@Component
class CurrentSession(val userDao: UserDao) {
    val user: User?
        get() {
            val auth = SecurityContextHolder.getContext().authentication
            val username = auth.name ?: throw RuntimeException("No hay usuario logueado")
            return userDao.findByUsername(username)
        }

    fun logInUser(userDetails: UserDetails) {
        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }
}