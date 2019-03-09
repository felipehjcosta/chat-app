import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.logging.LoggerAdapter
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import org.slf4j.LoggerFactory
import java.util.*
import java.util.Collections.synchronizedSet

fun main() {
    setupLogger()
    setupServer()
}

fun setupLogger() {
    Logger {
        loggerAdapter = object : LoggerAdapter {

            val loggerFactory = LoggerFactory.getLogger("Logger")

            override fun info(message: String) {
                loggerFactory.info(message)
            }
        }
    }
}

fun setupServer() {
    embeddedServer(Netty, port = 8080) {
        install(WebSockets)
        routing {
            get("/") { call.respondText("Hello World server!", ContentType.Text.Plain) }
            chat()
        }
    }.start(wait = true)
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

@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
suspend fun handleTextMessage(frame: Frame.Text, wsConnections: Set<DefaultWebSocketSession>) {
    val text = frame.readText()
    val message = Json.parse<Message>(text)
    Logger.info("Send message \"${message.message}\" from author \"${message.author}\"")
    wsConnections.forEach { it.outgoing.send(Frame.Text(text)) }
}
