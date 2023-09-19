package st.cri.app.controller.rest

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import st.cri.app.domain.user.RoleName
import st.cri.app.dto.user.ChangePassForm
import st.cri.app.dto.user.UserDTO
import st.cri.app.dto.user.UserForm
import st.cri.app.exception.DoesNotExistException
import st.cri.app.service.UserService
import st.cri.app.template.controller.TemplateController

@RestController
@RequestMapping(value = ["/usuarios"])
@Api(tags = ["usuarios-controller"])
class UserController(userService: UserService) :
    TemplateController<UserDTO, UserForm, UserService>(PAGE_SIZE, userService) {
    @PostMapping(value = ["guardar"])
    override fun save(@RequestBody form: UserForm): ResponseEntity<*> {
        return try {
            service.update(form.id!!, form)
            ResponseEntity.ok().build<Any>()
        } catch (e: DoesNotExistException) {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @PostMapping(value = ["activar"])
    fun activateAccount(@RequestBody form: ChangePassForm): ResponseEntity<*> {
        return try {
            val role: RoleName = service.activateUserAccount(form.token!!, form.password!!)
            ResponseEntity.ok<Any>(role)
        } catch (e: DoesNotExistException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body<String>(e.message)
        }
    }

    @PostMapping(value = ["password"])
    fun resetPassword(@RequestParam username: String): ResponseEntity<*> {
        return try {
            service.resetUserPassword(username)
            ResponseEntity.ok().build<Any>()
        } catch (e: DoesNotExistException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body<String>(e.message)
        }
    }

    companion object {
        private const val PAGE_SIZE = 15
    }
}
