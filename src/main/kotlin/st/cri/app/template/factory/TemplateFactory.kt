package st.cri.app.template.factory

/**
 * Factory que sabe como armar cada objeto (Patron Abstract Factory)
 *
 * @author jime
 *
 * @param <O>
 * Objeto del dominio
 * @param <Dto>
 * DTO para enviar a la vista
 * @param <Form>
 * Form que se recibe de la vista
</Form></Dto></O> */
interface TemplateFactory<O, Dto, Form> : TemplateDTOFactory<O, Dto> {
    fun assembleForm(obj: O): Form
    fun assembleNew(form: Form): O
    val blankForm: Form

    fun update(dbObject: O, updates: Form): O
}
