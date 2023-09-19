package st.cri.app.controller.rest

import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.web.bind.annotation.*
import st.cri.app.security.jwt.JwtRequest
import st.cri.app.service.service_impl.JwtUserDetailsService


@RestController
@CrossOrigin(origins = ["*"], methods = [RequestMethod.GET, RequestMethod.POST])
@Api(tags = ["authenticate"])
class JwtAuthenticationController(val userDetailsService: JwtUserDetailsService) {
    @PostMapping(value = ["/authenticate"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(
        Exception::class
    )
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtRequest): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(userDetailsService.authenticateUser(authenticationRequest))
        } catch (e: DisabledException) {
            ResponseEntity.badRequest().body("Usuario deshabilitado")
        } catch (e: BadCredentialsException) {
            ResponseEntity.badRequest().body("Usuario y/o contraseña incorrectos")
        }
    }
}
