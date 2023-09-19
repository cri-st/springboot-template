package st.cri.app.dao

import org.springframework.stereotype.Repository
import st.cri.app.domain.user.User
import st.cri.app.template.dao.TemplateDao
import java.util.*


@Repository
interface UserDao : TemplateDao<User, Long> {
    fun findByUsername(username: String): User?
    fun findByActivationToken(token: String): User?
}
