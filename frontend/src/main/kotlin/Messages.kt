import com.github.felipehjcosta.chatapp.Message
import react.RBuilder
import react.dom.div

fun RBuilder.messages(messages: List<Message>) = div(classes = "messages") {
    messages.map {
        div {
            +"${it.author}: ${it.message}"
        }
    }
}

