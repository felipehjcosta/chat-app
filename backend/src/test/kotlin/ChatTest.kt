import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.stringify
import io.ktor.application.Application
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import kotlin.test.assertEquals

class ChatTest {
    @Test(timeout = 10000L)
    fun testSimpleConversation() {
        withTestApplication(Application::installChat) {
            val log = arrayListOf<String>()

            handleWebSocketConversation("/chat") { incoming, outgoing ->
                val message = Message("WORLD", "HELLO")
                outgoing.send(Frame.Text(message.stringify()))

                for (n in 0 until 1) {
                    log += (incoming.receive() as Frame.Text).readText()
                }
            }

            assertEquals(
                    listOf(
                            Message("WORLD", "HELLO").stringify()
                    ), log
            )
        }
    }
}
