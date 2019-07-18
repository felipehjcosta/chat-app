import react.RBuilder
import react.dom.div

fun RBuilder.app() = div("App") {
    div(classes = "container") {
        div(classes = "row") {
            div(classes = "col-auto") {
                div(classes = "card") {
                    div(classes = "card-body") {
                        chat()
                    }
                }
            }
        }
    }
}

