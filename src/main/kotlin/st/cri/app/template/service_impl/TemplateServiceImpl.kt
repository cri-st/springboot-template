package st.cri.app.template.service_impl

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import org.apache.logging.log4j.LogManager
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl
import org.hibernate.query.criteria.internal.predicate.BooleanStaticAssertionPredicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import st.cri.app.exception.DoesNotExistException
import st.cri.app.template.dao.TemplateDao
import st.cri.app.template.dto.Selector
import st.cri.app.template.factory.TemplateFactory
import st.cri.app.template.service.TemplateService
import st.cri.app.template.view.ViewEntity
import java.util.*

abstract class TemplateServiceImpl<O : Any, Dto, Form : ViewEntity, Factory : TemplateFactory<O, Dto, Form>>(

) : TemplateService<Dto, Form> {
    @Autowired
    private lateinit var dao: TemplateDao<O, Long>
    @Autowired
    private lateinit var factory: Factory

    /**
     * Listado paginado y filtrado en caso de que filter sea distinto a null
     */
    fun getAll(filter: Form?, pageable: Pageable): Page<Dto> {
        val lista: MutableList<Dto> = ArrayList()
        val list: Page<O>
        list = if (filter != null) {
            dao.findAll(isFiltered(filter), pageable)
        } else {
            dao.findAll(pageable)
        }
        for (`object` in list) {
            lista.add(factory.assemble(`object`))
        }
        return PageImpl(lista, pageable, list.totalElements)
    }

    /**
     * Listado filtrado en caso de que filter sea distinto a null
     */
    fun getSelectorList(filter: Form?): List<Selector> {
        val listDTO: MutableList<Selector> = ArrayList<Selector>()
        val list: List<O>
        list = if (filter != null) {
            dao.findAll(isFiltered(filter))
        } else {
            dao.findAll()
        }
        for (`object` in list) {
            listDTO.add(factory.assembleSelector(`object`))
        }
        return listDTO
    }

    fun getFormById(id: Long?): Optional<Form> {
//		if(permissionEvaluator.hasPermission(SecurityContextHolder.getContext().getAuthentication(), id, getTargetDomainObject(), Permission.VIEW)){
        logger.trace("Buscando id {}", id)
        val optional: Optional<O> = dao.findById(id!!)
        if (optional.isPresent) {
            logger.trace("Se encontró por id: {}", id)
            return Optional.of(factory.assembleForm(optional.get()))
        }
        logger.warn("NO se encontró el id: {}", id)
        return Optional.empty()
        //		}else{
//			throw new RuntimeException("No tiene permisos para visualizacion");
//		}
    }

    fun findById(id: Long?): Optional<Dto> {
        logger.trace("Buscando id {}", id)
        val optional: Optional<O> = dao.findById(id!!)
        if (optional.isPresent) {
            logger.trace("Se encontró por id: {}", id)
            return Optional.of(factory.assemble(optional.get()))
        }
        logger.warn("NO se encontró el id: {}", id)
        return Optional.empty()
    }

    override val blankForm: Form
        get() = factory.blankForm

    @Transactional
    override fun create(form: Form): O {
//		if(permissionEvaluator.hasPermission(SecurityContextHolder.getContext().getAuthentication(), getTargetDomainObject(), Permission.CREATION)){
        logger.trace("Creando")
        val o: O = dao.saveAndFlush(factory.assembleNew(form))
        logger.trace("Creada")
        return o
        //		}else{
//			throw new RuntimeException("No tiene permisos para creacion");
//		}
    }

    @Transactional
    open fun update(id: Long?, form: Form): O {
//		if(permissionEvaluator.hasPermission(SecurityContextHolder.getContext().getAuthentication(),id, getTargetDomainObject(),  Permission.EDITION)){
        logger.trace("Editando ")
        val optional: Optional<O> = dao.findById(form.id!!)
        return if (optional.isPresent) {
            val dbObject = optional.get()
            dao.saveAndFlush(factory.update(dbObject, form))
            logger.trace("Se actualizó ")
            dbObject
        } else {
            throw DoesNotExistException()
        }
        //		}else{
//			throw new RuntimeException("No tiene permisos de edicion");
//		}
    }

    //	@Transactional
    open fun delete(id: Long?) {
//		if(permissionEvaluator.hasPermission(SecurityContextHolder.getContext().getAuthentication(),id, getTargetDomainObject(),  Permission.EDITION)){
        logger.trace("Borrando id {}", id)
        dao.deleteById(id!!)
        logger.trace("Id {} BORRADA", id)
        //		}else{
//			throw new RuntimeException("No tiene permisos de borrado");
//		}
    }

    open fun activate(id: Long?) {
        TODO("No esta implementado")
    }

    open fun deactivate(id: Long?) {
        TODO("No esta implementado")
    }

    private fun isFiltered(filter: Form): Specification<O> {
        return Specification { root: Root<O>?, query: CriteriaQuery<*>?, cb: CriteriaBuilder? ->
            filteredPredicate(
                root,
                query,
                cb,
                filter
            )
        }
    }

    //Por default Devuelve un predicado true para q siempre pase
    protected fun filteredPredicate(
        root: Root<O>?,
        query: CriteriaQuery<*>?,
        cb: CriteriaBuilder?,
        filter: Form
    ): Predicate {
        return BooleanStaticAssertionPredicate(cb as CriteriaBuilderImpl?, true) as Predicate
    }

    /***Para hacer el filtro generico */
    protected fun getLikeFilterPredicate(
        formValue: String?,
        formField: String?,
        root: Root<O>,
        cb: CriteriaBuilder
    ): Predicate {
        return if (formValue == null || formValue.isEmpty()) {
            BooleanStaticAssertionPredicate(cb as CriteriaBuilderImpl, true) as Predicate
        } else cb.like(cb.upper(root.get(formField)), formValue.uppercase(Locale.getDefault()) + "%")
    }

    protected fun getLikeFilterPredicate(
        formValue: String?,
        outerFormField: String?,
        innerFormField: String,
        root: Root<O>,
        cb: CriteriaBuilder
    ): Predicate {
        if (formValue == null || formValue.isEmpty()) {
            return BooleanStaticAssertionPredicate(cb as CriteriaBuilderImpl, true) as Predicate
        }
        var expression: Path<String?> = root.get(outerFormField)
        val fields = innerFormField.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (f in fields) {
            expression = expression.get(f)
        }
        return cb.like(cb.upper(expression), formValue.uppercase(Locale.getDefault()) + "%")
    }

    override fun isValid(form: Form): Boolean {
        return true
    }

    companion object {
        private val logger = LogManager.getLogger(
            TemplateServiceImpl::class.java
        )
    }
}
