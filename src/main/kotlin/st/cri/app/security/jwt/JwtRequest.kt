package st.cri.app.security.jwt

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable

@Data
@NoArgsConstructor
@AllArgsConstructor
class JwtRequest(val username: String, val password: String) : Serializable {
}
