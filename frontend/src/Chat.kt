import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatClient
import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*

external fun require(module: String): dynamic

class Chat : RComponent<RProps, Chat.State>() {

    private val sendButtonHandler: (Event) -> Unit = {
        it.preventDefault()
        sendMessage()

        setState {
            username = ""
            message = ""
        }
    }

    private lateinit var chatClient: ChatClient

    init {
        state = State()
    }

    override fun componentDidMount() {
        chatClient = ChatClient("ws://localhost:8080/chat").apply {
            receive(::receiveMessage)
            start()
        }
    }

    private fun sendMessage() {
        chatClient.send(Message(this.state.username, this.state.message))
    }

    private fun receiveMessage(newMessage: Message) {
        Logger.info("Message '${newMessage.message}' received from '${newMessage.author}'")
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
                                        value = state.username
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
                                        value = state.message
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

    class State(
            var username: String = "",
            var message: String = "",
            var messages: List<Message> = emptyList()
    ) : RState
}

fun RBuilder.chat() = child(Chat::class) {}