import kotlinext.js.js
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.navLink

class Welcome : RComponent<RProps, Welcome.WelcomeState>() {

    init {
        state = WelcomeState()
    }

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

                    attrs {
                        value = state.username
                        onChangeFunction = ::usernameInputOnChangeHandler
                    }
                }
                span(classes = "input-group-append") {
                    navLink("/chat/${state.username}") {
                        button(classes = "btn btn-outline-secondary", type = ButtonType.button) {
                            +"Login"
                        }
                    }
                }
            }
        }
    }

    private fun usernameInputOnChangeHandler(event: Event) {
        event.target.unsafeCast<HTMLInputElement>().value.run {
            setState {
                username = this@run
            }
        }
    }

    override fun RBuilder.render() {
        div(classes = "container") {
            renderTitle()
            renderNicknameInput()
        }
    }

    class WelcomeState(var username: String = "") : RState
}

fun RBuilder.welcome() = child(Welcome::class) {}
