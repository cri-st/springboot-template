package st.cri.app.dto.user

import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChangePassForm {
    @NotBlank(message = "Debe ingresar el token")
    val token: String? = null

    @NotBlank(message = "Debe ingresar una contraseña")
    val password: String? = null
}
