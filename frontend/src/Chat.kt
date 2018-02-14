import kotlinx.html.InputType
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class Chat : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "container") {
            div(classes = "row") {
                div(classes = "col-4") {
                    div(classes = "card") {
                        div(classes = "card-body") {
                            div(classes = "card-title") { +"Global Chat" }
                            hr {}
                            div(classes = "messages") {

                            }

                            div(classes = "card-footer") {
                                input(type = InputType.text,
                                        classes = "form-control") {
                                    attrs { placeholder = "Username" }
                                }
                                br {}
                                input(type = InputType.text,
                                        classes = "form-control") {
                                    attrs { placeholder = "Message" }
                                }
                                br {}
                                button(classes = "btn btn-primary form-control") {
                                    +"Send"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.chat() = child(Chat::class) {}