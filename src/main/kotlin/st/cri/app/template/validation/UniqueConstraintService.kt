package st.cri.app.template.validation

import st.cri.app.template.view.ViewEntity


interface UniqueConstraintService {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param value The value to check for
     * @param fieldName The name of the field for which to check if the value exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    @Throws(UnsupportedOperationException::class)
    fun fieldValueExists(value: Any, fieldName: String): Boolean

    @Throws(UnsupportedOperationException::class)
    fun fieldValueNotExistOther(value: ViewEntity?, fieldName: String): Boolean
}
