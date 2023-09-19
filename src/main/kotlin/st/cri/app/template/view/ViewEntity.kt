package st.cri.app.template.view

open class ViewEntity(var id: Long?) {
    fun validateAsEdition(): Boolean {
        return id != 0L
    }
}
