import kotlinext.js.js
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.h3
import react.dom.input
import react.dom.span
import react.router.dom.navLink
import react.setState

class Welcome : RComponent<RProps, Welcome.WelcomeState>() {

    init {
        state = WelcomeState()
    }

    private fun RBuilder.renderTitle() {
        div(classes = "row justify-content-md-center p-4") {
            h3(classes = "title") { +"Welcome to Awesome Chat App" }
        }
    }

    private fun RBuilder.renderWelcomeForm() {
        div(classes = "row justify-content-md-center") {
            div(classes = "input-group welcome-input-group") {
                attrs["style"] = js { width = "50%" }

                renderUsernameInput()
                renderLoginButton()
            }
        }
    }

    private fun RBuilder.renderUsernameInput() {
        input(classes = "form-control text-center", type = InputType.search) {
            attrs.placeholder = "Enter your nickname"

            attrs {
                value = state.username
                onChangeFunction = ::usernameInputOnChangeHandler
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

    private fun RBuilder.renderLoginButton() {
        span(classes = "input-group-append") {
            navLink("/chat/${state.username}") {
                button(classes = "btn btn-outline-secondary", type = ButtonType.button) {
                    +"Login"
                }
            }
        }
    }

    override fun RBuilder.render() {
        div(classes = "container") {
            renderTitle()
            renderWelcomeForm()
        }
    }

    class WelcomeState(var username: String = "") : RState
}

fun RBuilder.welcome() = child(Welcome::class) {}
