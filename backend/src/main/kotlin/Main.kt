import com.github.felipehjcosta.chatapp.Message
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import java.util.*

@ImplicitReflectionSerializer
fun main() {
    embeddedServer(Netty, port = 8080) {
        install(WebSockets)
        routing {

            get("/") {
                call.respondText("Hello World server!", ContentType.Text.Plain)
            }
            val wsConnections = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())

            webSocket("/chat") {
                wsConnections += this
                try {
                    while (true) {
                        when (val frame = incoming.receive()) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                val message = JSON.parse<Message>(text)
                                println("Send message \"${message.message}\" from author \"${message.author}\"")
                                wsConnections.forEach { it.outgoing.send(Frame.Text(text)) }
                            }
                        }
                    }
                } finally {
                    wsConnections -= this
                }
            }
        }
    }.start(wait = true)
}