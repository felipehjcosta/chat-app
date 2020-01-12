import com.github.felipehjcosta.chatapp.Message
import io.ktor.application.Application
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Test
import kotlin.test.assertEquals

class ChatTest {
    @UseExperimental(kotlinx.serialization.UnstableDefault::class)
    @Test(timeout = 10000L)
    fun testSimpleConversation() {
        // First we create a [TestApplicationEngine] that includes the module [Application.main],
        // this executes that function and thus installs all the features and routes to this test application.
        @UseExperimental(ImplicitReflectionSerializer::class)
        withTestApplication(Application::main) {
            // Keeps a log array that will hold all the events we want to check later at once.
            val log = arrayListOf<String>()

            // We perform a test websocket connection to this route. Effectively acting as a client.
            // The [incoming] parameter allows to receive frames, while the [outgoing] allows to send frames to the server.

            handleWebSocketConversation("/chat") { incoming, outgoing ->
                // Send a HELLO message
                val message = Message("WORLD", "HELLO")
                outgoing.send(Frame.Text(Json.stringify(Message::class.serializer(), message)))

                // We then receive two messages (the message notifying that the member joined, and the message we sent echoed to us)
                for (n in 0 until 1) {
                    log += (incoming.receive() as Frame.Text).readText()
                }
            }

            // Verify the received messages.
            assertEquals(
                    listOf(
                            Json.stringify(Message::class.serializer(),Message("WORLD", "HELLO"))
                    ), log
            )
        }
    }
}