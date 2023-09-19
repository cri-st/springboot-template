package st.cri.app.template.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import st.cri.app.service.service_impl.UserServiceImpl
import kotlin.reflect.KClass


@Constraint(validatedBy = arrayOf(UniqueOnNewValidator::class))
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UniqueOnNew(
    val message: String = "{unique.value.violation}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val service: KClass<UserServiceImpl>,
    val serviceQualifier: String = "",
    val fieldName: String
)
