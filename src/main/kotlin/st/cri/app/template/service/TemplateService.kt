package st.cri.app.template.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import st.cri.app.template.dto.Selector
import st.cri.app.template.validation.UniqueConstraintService
import st.cri.app.template.view.ViewEntity
import java.util.*

interface TemplateService<Dto, Form> : UniqueConstraintService {
    fun getAll(filter: Form, pageable: PageRequest?): Page<Dto>
    fun getSelectorList(filter: Form): List<Selector>
    fun getFormById(id: Long): Optional<Form>
    fun findById(id: Long): Optional<Dto>
    fun create(form: Form): Any
    fun update(id: Long, form: Form): Any
    fun delete(id: Long)
    fun deactivate(id: Long)
    fun activate(id: Long)
    val blankForm: Form

    fun isValid(form: Form): Boolean
}
