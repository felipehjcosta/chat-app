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

    private val chatViewModel: ChatViewModel by ChatInjector.viewModel()

    private var userNameInputElement: Element? = null
    private var messageInputElement: Element? = null
    private var sendButtonElement: Element? = null

    private val userNameInputOnKeyUpHandler: (Event) -> Unit = {
        if (it.asDynamic().key == "Enter") {
            it.preventDefault()
            messageInputElement.asDynamic().focus()
        }
    }

    private val messageInputOnKeyUpHandler: (Event) -> Unit = {
        if (it.asDynamic().key == "Enter") {
            it.preventDefault()
            sendButtonElement.asDynamic().click()
            messageInputElement.asDynamic().blur()
        }
    }

    private val userNameInputOnChangeHandler: (Event) -> Unit = {
        it.target.unsafeCast<HTMLInputElement>().value.run {
            setState {
                username = this@run
            }
        }

    }

    private val messageInputOnChangeHandler: (Event) -> Unit = {
        it.target.unsafeCast<HTMLInputElement>().value.run {
            setState {
                message = this@run
            }
        }
    }

    private val sendButtonOnClickHandler: (Event) -> Unit = {
        it.preventDefault()
        sendButtonElement.asDynamic().blur()
        sendMessage()

        setState {
            username = ""
            message = ""
        }
    }
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

                                    ref {
                                        userNameInputElement = findDOMNode(it)
                                    }

                                    attrs {
                                        placeholder = "Username"
                                        value = state.username
                                        onChangeFunction = userNameInputOnChangeHandler
                                        onKeyUpFunction = userNameInputOnKeyUpHandler
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
                                        onChangeFunction = messageInputOnChangeHandler
                                        onKeyUpFunction = messageInputOnKeyUpHandler
                                    }
                                }
                                br {}
                                button(classes = "btn btn-primary form-control") {
                                    +"Send"

                                    ref {
                                        sendButtonElement = findDOMNode(it)
                                    }

                                    attrs {
                                        onClickFunction = sendButtonOnClickHandler
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