import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.Serializable
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
        receiveMessage(Message(state.username, state.message))
    }

    private fun receiveMessage(newMessage: Message) {
        console.log(">>> Message '${newMessage.message}' from '${newMessage.author}' received")
        setState {
            messages = messages.toMutableList().apply { add(newMessage) }
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
                                        +"${it.author}: ${it.message}"
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
                var messages: List<Message> = emptyList()) : RState
}

@Serializable
data class Message(val author: String, val message: String)

fun RBuilder.chat() = child(Chat::class) {}