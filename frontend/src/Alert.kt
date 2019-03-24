import react.RBuilder
import react.dom.div

fun RBuilder.connectionAlert(showConnectionAlert: Boolean = false) = if (showConnectionAlert) {
    div(classes = "alert alert-danger") {
        +"Connection with the server failed."
    }
} else {
    empty
}
