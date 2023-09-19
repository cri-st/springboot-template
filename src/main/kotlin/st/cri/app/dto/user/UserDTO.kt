package st.cri.app.dto.user

import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import st.cri.app.domain.user.ActivationState
import st.cri.app.domain.user.RoleName
import st.cri.app.template.view.ViewEntity

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
class UserDTO(id: Long?, private val username: String, val rolename: RoleName, val state: ActivationState, val email: String) : ViewEntity(id) {
}
