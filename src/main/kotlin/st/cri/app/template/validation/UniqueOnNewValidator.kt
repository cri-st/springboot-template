package st.cri.app.template.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.context.ApplicationContext
import st.cri.app.template.view.ViewEntity


class UniqueOnNewValidator(private val applicationContext: ApplicationContext) :
    ConstraintValidator<UniqueOnNew?, ViewEntity?> {
    private var service: UniqueConstraintService? = null
    private var fieldName: String? = null
    fun initialize(unique: UniqueOnNew) {
        val clazz: Class<out UniqueConstraintService?> = unique.service.java
        fieldName = unique.fieldName
        val serviceQualifier: String = unique.serviceQualifier
        if (serviceQualifier != "") {
            service = applicationContext.getBean(serviceQualifier, clazz)
        } else {
            service = applicationContext.getBean(clazz)
        }
    }

    override fun isValid(`object`: ViewEntity?, context: ConstraintValidatorContext?): Boolean {
        return service!!.fieldValueNotExistOther(`object`, fieldName!!)
    }
}
