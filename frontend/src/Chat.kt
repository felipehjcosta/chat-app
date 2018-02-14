import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*

class Chat : RComponent<RProps, Chat.State>() {

    private val sendButtonHandler: (Event) -> Unit = {
        it.preventDefault()
        sendMessage()

        setState {
            message = ""
        }
    }

    init {
        state = State()

    }

    private fun sendMessage() {
        receiveMessage(mapOf("author" to state.username, "message" to state.message))
    }

    private fun receiveMessage(data: Map<String, String>) {
        console.log(">>> Message '${data["message"]}' from '${data["author"]}' received")
        setState {
            messages = messages.toMutableList().apply { add(data["author"]!! to data["message"]!!) }
        }
    }

    override fun RBuilder.render() {
        div(classes = "container") {
            div(classes = "row") {
                div(classes = "col-4") {
                    div(classes = "card") {
                        div(classes = "card-body") {
                            div(classes = "card-title") { +"Global Chat" }
                            hr {}
                            div(classes = "messages") {
                                state.messages.map {
                                    div {
                                        +"${it.first}: ${it.second}"
                                    }
                                }
                            }

                            div(classes = "card-footer") {
                                input(type = InputType.text,
                                        classes = "form-control") {
                                    attrs {
                                        placeholder = "Username"
                                        defaultValue = state.username
                                        onChangeFunction = {

                                            val value = it.target.unsafeCast<HTMLInputElement>().value
                                            setState {
                                                username = value
                                            }
                                        }
                                    }
                                }
                                br {}
                                input(type = InputType.text,
                                        classes = "form-control") {
                                    attrs {
                                        placeholder = "Message"
                                        defaultValue = state.message
                                        onChangeFunction = {
                                            val value = it.target.unsafeCast<HTMLInputElement>().value
                                            setState {
                                                message = value
                                            }
                                        }
                                    }
                                }
                                br {}
                                button(classes = "btn btn-primary form-control") {
                                    +"Send"

                                    attrs {
                                        onClickFunction = sendButtonHandler
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    class State(var username: String = "",
                var message: String = "",
                var messages: List<Pair<String, String>> = emptyList()) : RState
}

fun RBuilder.chat() = child(Chat::class) {}