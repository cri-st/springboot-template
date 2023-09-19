package st.cri.app.domain.user

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import st.cri.app.domain.GenericEntity
import st.cri.app.exception.DoesNotExistException

@Access(AccessType.FIELD)
@Table(name = "user")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
data class User(
    @field:Column(nullable = false, unique = true) private var username: String,
    @Column private var password: String,
    @Enumerated(EnumType.STRING) var state: ActivationState,
    @Column(name = "activation_token") var activationToken: String?,
    @Column(name = "email") var email: String,
    @ManyToOne @JoinColumn(name = "role_fk", nullable = false) var role: Role
) : GenericEntity(), UserDetails {
    constructor(
        email: String,
        role: Role
    ) : this(email, "#########", ActivationState.VERIFY_TOKEN, RandomStringUtils.randomAlphanumeric(20), email, role) {
    }
    override fun getUsername(): String = username
    override fun getPassword(): String = password
    override fun isEnabled(): Boolean = isEnabled
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = ArrayList<SimpleGrantedAuthority>()
        authorities.add(SimpleGrantedAuthority("ROLE_" + role.name))
        return authorities
    }

    fun activate(password: String, passwordEncoder: PasswordEncoder) {
        if (state.equals(ActivationState.INACTIVE)) throw DoesNotExistException("El usuario debe ser activado por un administrador")
        state = ActivationState.ACTIVE
        this.password = passwordEncoder.encode(password)
        activationToken = null
    }
    fun deactivate() {
        state = ActivationState.INACTIVE
    }
    fun activate() {
        state = ActivationState.ACTIVE
    }
    fun resetPassword() {
        activationToken = RandomStringUtils.randomAlphanumeric(20)
    }
}