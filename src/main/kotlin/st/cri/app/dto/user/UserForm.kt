package st.cri.app.dto.user

import jakarta.validation.constraints.Email
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import st.cri.app.domain.user.ActivationState
import st.cri.app.domain.user.RoleName
import st.cri.app.service.service_impl.UserServiceImpl
import st.cri.app.template.validation.UniqueOnNew
import st.cri.app.template.view.ViewEntity


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@UniqueOnNew(service = UserServiceImpl::class, fieldName = "email", message = "Ya existe un usuario con ese email")
class UserForm(
    id: Long?,
    @field:Email(message = "Debe ingresar un email válido") val email: String?,
    val password: String?,
    val rolename: RoleName?,
    val keywords: List<String>?,
    val state: ActivationState? = null
) : ViewEntity(id) {
    constructor() : this(null, null, null, null, null, null)
}
