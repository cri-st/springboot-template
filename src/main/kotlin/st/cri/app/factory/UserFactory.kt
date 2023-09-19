package st.cri.app.factory

import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import st.cri.app.dao.RoleDao
import st.cri.app.domain.user.Role
import st.cri.app.domain.user.RoleName
import st.cri.app.domain.user.User
import st.cri.app.dto.user.UserDTO
import st.cri.app.dto.user.UserForm
import st.cri.app.template.dto.Selector
import st.cri.app.template.factory.TemplateFactory
import java.util.regex.Pattern

@Component
class UserFactory(val roleDao: RoleDao) : TemplateFactory<User, UserDTO, UserForm> {
    private val logger = LogManager.getLogger(UserFactory::class.java)

    override fun assemble(obj: User): UserDTO {
        return UserDTO(obj.id, obj.username, obj.role.name, obj.state, obj.email)
    }

    override fun assembleNew(form: UserForm): User {
        val role: Role = roleDao.findByName(RoleName.STANDARD) // Registro no debería poder elegir el rol
        return User(form.email!!, role)
    }

    override fun assembleForm(obj: User): UserForm {
        TODO("Not yet implemented")
        return UserForm()
    }

    override val blankForm: UserForm
        get() = UserForm()

    override fun update(dbObject: User, updates: UserForm): User {
        if (!StringUtils.isEmpty(updates.email)) {
            if (!Pattern.compile(EMAIL_REGEX).matcher(updates.email!!).matches()) {
                throw UnsupportedOperationException("El email no es válido")
            }
            dbObject.email = updates.email
        }
        if (updates.rolename != null) {
            val role: Role = roleDao.findByName(updates.rolename)
            dbObject.role = role
        }
        return dbObject
    }

    override fun assembleSelector(obj: User): Selector {
        return Selector.makeSelector(obj.role.name.toString(), "rolename")
    }

    companion object {
        const val EMAIL_REGEX = "^(.+)@(\\S+)$"
    }
}
