package st.cri.app.utils

abstract class PathBuilder {
    fun makePath(host: String?, relativeLink: String?): String {
        var host = host
        var relativeLink = relativeLink
        if (host != null && host.endsWith("/")) {
            host = host.substring(0, host.length - 1)
        }
        if (relativeLink != null && relativeLink.startsWith("/") && relativeLink.length > 1) {
            relativeLink = relativeLink.substring(1)
        }
        return "$host/$relativeLink"
    }
}