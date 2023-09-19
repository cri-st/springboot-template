package st.cri.app.template.dto

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Selector(private var value: String, private var label: String) : Comparable<Selector?> {
    override operator fun compareTo(other: Selector?): Int {
        return label.compareTo(other!!.label)
    }

    companion object {
        fun makeSelector(value: String, label: String): Selector {
            return Selector(value, label)
        }
    }
}
