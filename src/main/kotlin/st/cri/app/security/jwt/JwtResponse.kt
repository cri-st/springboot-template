package st.cri.app.security.jwt

import st.cri.app.domain.user.RoleName
import java.io.Serializable


class JwtResponse(val token: String, role: RoleName) : Serializable {
    private val role: RoleName

    init {
        this.role = role
    }

    val userRole: RoleName
        get() = role
}
