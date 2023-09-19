package st.cri.app.domain.user

import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.Getter
import st.cri.app.domain.GenericEntity


@Access(AccessType.FIELD)
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
class Role(
    @Enumerated(EnumType.STRING) val name: RoleName
) : GenericEntity() {
}
