import kotlinext.js.js
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.h3
import react.dom.input
import react.dom.span

class Welcome : RComponent<RProps, RState>() {

    private fun RBuilder.renderTitle() {
        div(classes = "row justify-content-md-center p-4") {
            h3(classes = "title") { +"Welcome to Awesome Chat App" }
        }
    }

    private fun RBuilder.renderNicknameInput() {
        div(classes = "row justify-content-md-center") {
            div(classes = "input-group welcome-input-group") {
                attrs["style"] = js { width = "50%" }

                input(classes = "form-control text-center", type = InputType.search) {
                    attrs.placeholder = "Enter your nickname"
                }
                span(classes = "input-group-append") {
                    button(classes = "btn btn-outline-secondary", type = ButtonType.button) {
                        +"Login"
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        div(classes = "container") {
            renderTitle()
            renderNicknameInput()
        }
    }
}

fun RBuilder.welcome() = child(Welcome::class) {}
