package st.cri.app.domain.user

import lombok.Getter

@Getter
enum class ActivationState(private val spanish: String,
                           private val internal: Boolean) {
    ACTIVE("Activo", false),
    INACTIVE("Inactivo", false),
    VERIFY_TOKEN("Esperando activación", true);

    val isActive: Boolean
        get() = ACTIVE == this
}
