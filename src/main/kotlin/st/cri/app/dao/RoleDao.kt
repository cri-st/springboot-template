package st.cri.app.dao

import org.springframework.stereotype.Repository
import st.cri.app.domain.user.Role
import st.cri.app.domain.user.RoleName
import st.cri.app.template.dao.TemplateDao

@Repository
interface RoleDao : TemplateDao<Role, Long> {
    fun findByName(roleName: RoleName): Role
}
