package st.cri.app.service

import st.cri.app.domain.user.RoleName
import st.cri.app.dto.user.UserDTO
import st.cri.app.dto.user.UserForm
import st.cri.app.template.service.TemplateService

interface UserService : TemplateService<UserDTO, UserForm> {
    fun activateUserAccount(token: String, password: String): RoleName
    fun resetUserPassword(username: String)
    val currentUserInSession: UserDTO
}
