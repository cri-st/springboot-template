package st.cri.app.template.factory

import st.cri.app.template.dto.Selector

interface TemplateDTOFactory<O, Dto> {
    fun assemble(obj: O): Dto
    fun assembleSelector(obj: O): Selector
}
