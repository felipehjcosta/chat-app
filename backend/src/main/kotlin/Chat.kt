import com.github.felipehjcosta.chatapp.toMessage
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Routing
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import org.slf4j.LoggerFactory
import java.util.*
import java.util.Collections.synchronizedSet

private val LOGGER = LoggerFactory.getLogger("ChatBackend")

fun Application.installChat() {
    install(WebSockets)
    routing { chat() }
}

fun Routing.chat() {
    val wsConnections = synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())
    webSocket("/chat") {
        wsConnections += this
        try {
            while (true) {
                when (val frame = incoming.receive()) {
                    is Frame.Text -> handleTextMessage(frame, wsConnections)
                }
            }
        } finally {
            wsConnections -= this
        }
    }
}

suspend fun handleTextMessage(frame: Frame.Text, wsConnections: Set<DefaultWebSocketSession>) {
    val text = frame.readText()
    val message = text.toMessage()
    LOGGER.info("Send message \"${message.message}\" from author \"${message.author}\"")
    wsConnections.forEach { it.outgoing.send(Frame.Text(text)) }
}
