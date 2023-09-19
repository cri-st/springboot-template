package st.cri.app.service.service_impl

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import st.cri.app.dao.UserDao
import st.cri.app.domain.user.User
import st.cri.app.security.jwt.JwtRequest
import st.cri.app.security.jwt.JwtResponse
import st.cri.app.security.jwt.JwtTokenUtil

@Service
class JwtUserDetailsService(val userDao: UserDao, val jwtTokenUtil: JwtTokenUtil) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): User {
        return userDao.findByUsername(username)?.let { it } ?: run {
            throw UsernameNotFoundException(
                String.format(
                    "No se encontro el usuario %s",
                    username
                )
            )
        }
    }

    fun authenticateUser(authenticationRequest: JwtRequest): JwtResponse {
        val user: User = loadUserByUsername(authenticationRequest.username)
        val token: String = jwtTokenUtil.generateToken(user)
        return JwtResponse(token, user.role.name)
    }
}
