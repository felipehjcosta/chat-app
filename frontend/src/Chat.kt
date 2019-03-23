import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.br
import react.dom.button
import react.dom.div
import react.dom.findDOMNode
import react.dom.hr
import react.dom.input
import react.setState

external fun require(module: String): dynamic

class Chat : RComponent<RProps, Chat.State>() {

    private val sendButtonHandler: (Event) -> Unit = {
        it.preventDefault()
        sendButtonElement.asDynamic().blur()
        sendMessage()

        setState {
            username = ""
            message = ""
        }
    }

    private val chatViewModel: ChatViewModel by ChatInjector.viewModel()
    private var messageInputElement: Element? = null
    private var sendButtonElement: Element? = null

    init {
        state = State()
    }

    override fun componentDidMount() {
        chatViewModel.run {
            onChat = this@Chat::receiveMessage
            showFailureMessage = { receiveFailure() }
            start()
        }
    }

    private fun sendMessage() {
        chatViewModel.sendMessage(Message(this.state.username, this.state.message))
    }

    private fun receiveMessage(newMessage: Message) {
        Logger.info("Message '${newMessage.message}' received from '${newMessage.author}'")
        setState {
            messages = messages.toMutableList().apply { add(newMessage) }
        }
    }

    private fun receiveFailure() {
        setState {
            hasFailure = true
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
                                input(
                                    type = InputType.text,
                                    classes = "form-control"
                                ) {
                                    attrs {
                                        placeholder = "Username"
                                        value = state.username
                                        onChangeFunction = {

                                            val value = it.target.unsafeCast<HTMLInputElement>().value
                                            setState {
                                                username = value
                                            }
                                        }

                                        onKeyUpFunction = {
                                            if (it.asDynamic().key == "Enter") {
                                                it.preventDefault()
                                                messageInputElement.asDynamic().focus()
                                            }

                                        }
                                    }
                                }
                                br {}
                                input(
                                    type = InputType.text,
                                    name = "message",
                                    classes = "form-control"
                                ) {
                                    ref {
                                        messageInputElement = findDOMNode(it)
                                    }
                                    attrs {
                                        placeholder = "Message"
                                        value = state.message
                                        onChangeFunction = {
                                            val value = it.target.unsafeCast<HTMLInputElement>().value
                                            setState {
                                                message = value
                                            }
                                        }

                                        onKeyUpFunction = {
                                            if (it.asDynamic().key == "Enter") {
                                                it.preventDefault()
                                                sendButtonElement.asDynamic().click()
                                                messageInputElement.asDynamic().blur()
                                            }
                                        }
                                    }
                                }
                                br {}
                                button(classes = "btn btn-primary form-control") {
                                    +"Send"

                                    ref {
                                        sendButtonElement = findDOMNode(it)
                                    }

                                    attrs {
                                        onClickFunction = sendButtonHandler
                                        disabled = state.hasFailure
                                    }

                                }

                            }
                            if (state.hasFailure) {
                                div(classes = "alert alert-danger") {
                                    +"Connection with the server failed."
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
        var messages: List<Message> = emptyList(),
        var hasFailure: Boolean = false
    ) : RState
}

fun RBuilder.chat() = child(Chat::class) {}