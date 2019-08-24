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

    private fun userNameInputOnKeyUpHandler(event: Event) {
        if (event.asDynamic().key == "Enter") {
            event.preventDefault()
            messageInputElement.asDynamic().focus()
        }
    }

    private fun messageInputOnKeyUpHandler(event: Event) {
        if (event.asDynamic().key == "Enter") {
            event.preventDefault()
            sendButtonElement.asDynamic().click()
            messageInputElement.asDynamic().blur()
        }
    }

    private fun userNameInputOnChangeHandler(event: Event) {
        event.target.unsafeCast<HTMLInputElement>().value.run {
            setState {
                username = this@run
            }
        }
    }

    private fun messageInputOnChangeHandler(event: Event) {
        event.target.unsafeCast<HTMLInputElement>().value.run {
            setState {
                message = this@run
            }
        }
    }

    private fun sendButtonOnClickHandler(event: Event) {
        event.preventDefault()
        sendButtonElement.asDynamic().blur()
        sendMessage()

        setState {
            username = ""
            message = ""
        }
    }

    private fun RBuilder.renderTitle() = div(classes = "card-title") { +"Global Chat" }

    private fun RBuilder.renderUserNameInput() {
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
                onChangeFunction = ::userNameInputOnChangeHandler
                onKeyUpFunction = ::userNameInputOnKeyUpHandler
            }
        }
    }

    private fun RBuilder.renderMessageInput() {
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
                onChangeFunction = ::messageInputOnChangeHandler
                onKeyUpFunction = ::messageInputOnKeyUpHandler
            }
        }
    }

    private fun RBuilder.renderSendButton() {
        button(classes = "btn btn-primary form-control") {
            +"Send"

            ref {
                sendButtonElement = findDOMNode(it)
            }

            attrs {
                onClickFunction = ::sendButtonOnClickHandler
                disabled = state.hasFailure
            }

        }
    }

    override fun RBuilder.render() {
        renderTitle()
        hr {}
        messages(state.messages)

        div(classes = "card-footer") {
            renderUserNameInput()
            br {}
            renderMessageInput()
            br {}
            renderSendButton()
        }
        connectionAlert(state.hasFailure)
    }

    class State(
        var username: String = "",
        var message: String = "",
        var messages: List<Message> = emptyList(),
        var hasFailure: Boolean = false
    ) : RState
}

fun RBuilder.chat() = child(Chat::class) {}
