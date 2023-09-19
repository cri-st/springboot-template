package st.cri.app.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class URLBuilder : PathBuilder() {
    @Value("\${application.front.host}")
    val host: String? = null

    fun makeOfflineAbsolutePathLink(relativeLink: String?): String {
        return makePath(host, relativeLink)
    }

    fun offlineAbsoluteActiveAccountPathLink(token: String): String {
        return makeOfflineAbsolutePathLink("activar/$token")
    }

    fun offlineAbsoluteResetPasswordPathLink(token: String): String {
        return makeOfflineAbsolutePathLink("password/$token")
    }
}
