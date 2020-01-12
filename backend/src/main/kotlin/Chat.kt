import com.github.felipehjcosta.chatapp.Message
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.*
import java.util.Collections.synchronizedSet

private val LOGGER = LoggerFactory.getLogger("ChatBackend")

fun Application.main() {
    install(WebSockets)
    install(CallLogging) {
        level = Level.INFO
    }
    routing {
        get("/") { call.respondText("Hello World server!", ContentType.Text.Plain) }
        chat()
    }
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

@UseExperimental(ImplicitReflectionSerializer::class)
suspend fun handleTextMessage(frame: Frame.Text, wsConnections: Set<DefaultWebSocketSession>) {
    val text = frame.readText()
    val message = Json.parse<Message>(text)
    LOGGER.info("Send message \"${message.message}\" from author \"${message.author}\"")
    wsConnections.forEach { it.outgoing.send(Frame.Text(text)) }
}
