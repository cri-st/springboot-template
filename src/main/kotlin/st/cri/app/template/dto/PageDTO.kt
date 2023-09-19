package st.cri.app.template.dto

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@NoArgsConstructor
@AllArgsConstructor
class PageDTO<T>(
    private val currentPage: Int,
    private val totalElements: Long,
    private val totalPages: Int,
    private val list: List<T>) {
}
