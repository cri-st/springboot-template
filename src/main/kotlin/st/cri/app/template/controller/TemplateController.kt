package st.cri.app.template.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.apache.logging.log4j.LogManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import st.cri.app.exception.DoesNotExistException
import st.cri.app.template.dto.PageDTO
import st.cri.app.template.service.TemplateService
import st.cri.app.template.view.ViewEntity


abstract class TemplateController<Result : ViewEntity, Form : ViewEntity, Service : TemplateService<Result, Form>>(
    pageSize: Int,
    service: Service
) {
    protected var service: Service
    private val pageSize: Int?
    private var sort: Sort? = null

    init {
        this.pageSize = pageSize
        this.service = service
    }

    protected constructor(pageSize: Int, service: Service, sort: Sort?) : this(pageSize, service) {
        this.sort = sort
    }

//    @Operation(summary = "Obtener la lista sin paginar")
//    @GetMapping(value = [""])
//    fun list(): ResponseEntity<PageDTO<Result>> {
//        val list: List<Result> = service.getAll(null)
//        return ResponseEntity.ok<PageDTO<Result>>(makePageDTO(list))
//    }
//
//    @Operation(summary = "Obtener la lista sin paginar")
//    @GetMapping(value = ["lista"])
//    fun list(filter: Form): ResponseEntity<PageDTO<Result>> {
//        val list: List<Result> = service.getAll(filter)
//        return ResponseEntity.ok<PageDTO<Result>>(makePageDTO(list))
//    }

    @Operation(summary = "Obtener la lista paginada lista/{nro de pagina}/{cantidad por pagina} ")
    @GetMapping(value = ["lista/{page}/{size}"])
    fun list(
        @PathVariable(name = "page", required = false) page: Long,
        @PathVariable(name = "size", required = false) size: Int?,
        @ModelAttribute form: Form
    ): ResponseEntity<PageDTO<Result>> {
        return list(form, page, size)
    }

    protected fun list(filter: Form, page: Long, size: Int?): ResponseEntity<PageDTO<Result>> {
        val listPage: Page<Result> = service.getAll(filter, getPageRequest(page, size))
        return if (!listPage.hasContent() && page > 1) {
            ResponseEntity.notFound().build<PageDTO<Result>>()
        } else ResponseEntity.ok<PageDTO<Result>>(makePageDTO(listPage))
    }

    protected fun makePageDTO(listPage: Page<Result>): PageDTO<Result> {
        var currentPage = 1
        val totalPages = listPage.totalPages
        if (totalPages > 1) {
            currentPage = listPage.number + 1
        }
        return PageDTO(currentPage, listPage.totalElements, totalPages, listPage.content)
    }

    protected fun makePageDTO(list: List<Result>): PageDTO<Result> {
        val currentPage = 1
        val totalPages = if (list.isEmpty()) 0 else 1
        return PageDTO(currentPage, list.size.toLong(), totalPages, list)
    }

    @Operation(summary = "Obtener elemento nuevo. (Para objetos que pueden necesitar venir precargados)")
    @GetMapping(value = ["nuevo"])
    fun newForm(): ResponseEntity<Form> {
        return ResponseEntity.ok(service.blankForm)
    }

    @Operation(summary = "Crear elemento nuevo.")
    @PostMapping(value = ["nuevo"])
    fun create(@Valid @RequestBody form: Form): ResponseEntity<*> {
        service.create(form)
        return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    }

    @Operation(summary = "Obtener los datos para editar elemento por id.")
    @GetMapping("editar/{id}")
    fun editable(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Form> {
        return ResponseEntity.of(service.getFormById(id))
    }

    @Operation(summary = "Update del elemento.")
    @PostMapping(value = ["guardar"])
    open fun save(@Valid @RequestBody form: Form): ResponseEntity<*> {
        return try {
            service.update(form.id!!, form)
            ResponseEntity.ok().build<Any>()
        } catch (e: DoesNotExistException) {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @Operation(summary = "Mostrar por id")
    @GetMapping("buscar/{id}")
    fun findOne(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Result> {
        return ResponseEntity.of(service.findById(id))
    }

    @Operation(summary = "Borrar por id")
    @PostMapping(value = ["borrar/{id}"])
    fun delete(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<*> {
        try {
            service.delete(id)
            return ResponseEntity.ok().build<Any>()
        } catch (e: Exception) {
            logger.error("Error al intentar borrar id: {}", id, e)
        }
        return ResponseEntity.badRequest().build<Any>()
    }

    @Operation(summary = "Desactivar por id")
    @PostMapping(value = ["disable/{id}"])
    fun desactivar(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<*> {
        try {
            service.deactivate(id)
            return ResponseEntity.ok().build<Any>()
        } catch (e: Exception) {
            logger.error("Error al intentar borrar id: {}", id, e)
        }
        return ResponseEntity.badRequest().build<Any>()
    }

    @Operation(summary = "Activar por id")
    @PostMapping(value = ["enable/{id}"])
    fun activar(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<*> {
        try {
            service.activate(id)
            return ResponseEntity.ok().build<Any>()
        } catch (e: Exception) {
            logger.error("Error al intentar borrar id: {}", id, e)
        }
        return ResponseEntity.badRequest().build<Any>()
    }

    protected val listName: String
        protected get() = "lista"

    /**
     * Metodo que los hijos pueden sobreescribir si quieren utilizar
     * validaciones especiales
     *
     * @param form
     * Form a validar
     * @param result
     * resultado de la validacion
     */
    protected fun validate(form: Form, result: BindingResult?) {}

    /**
     * Metodo que los hijos pueden sobreescribir para poder manejar errores
     * previos a la carga
     *
     * @return Si tiene errores o no
     */
    fun hasPreCargaErrors(): Boolean {
        return false
    }

    protected fun getPageRequest(page: Long?, size: Int?): PageRequest? {
        var page = page
        if (size == null && pageSize == null) return null
        if (page == null) page = 1L
        if (sort == null) {
            sort = defaultSort
        }
        return PageRequest.of(page.toInt() - 1, (size ?: pageSize)!!, sort!!)
    }

    val defaultSort: Sort
        get() = Sort.by(Sort.Direction.DESC, "id")

    companion object {
        private val logger = LogManager.getLogger(
            TemplateController::class.java
        )
    }
}
