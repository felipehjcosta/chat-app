import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatClient
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage
import kotlinx.io.IOException
import kotlinx.serialization.ImplicitReflectionSerializer
import org.w3c.dom.WebSocket
import kotlin.js.Promise
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ChatClientTest {

    private val fakeURL = "ws://localhost:8080"

    private val mockServer = Server(fakeURL)

    @BeforeTest
    fun setUp() {
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test
    fun ensureAStartedConnectionIsOpenedByClient() = Promise<Unit> { resolve, _ ->
        mockServer.on("connection") { socket ->
            assertEquals(WebSocket.OPEN, socket.readyState)
            mockServer.stop()
            resolve(Unit)
        }

        ChatClient(fakeURL).apply { start() }
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test
    fun ensureSendMessageIsReceivedByServer() = Promise<Unit> { resolve, _ ->
        val messageSent = Message("Test", "Hello, WebSockets!")
        mockServer.on("connection") { socket ->
            socket.on("message") { data ->
                assertEquals(messageSent, data.toString().toMessage())
                mockServer.stop()
                resolve(Unit)
            }
        }

        ChatClient(fakeURL).apply {
            start()
            send(messageSent)
        }
    }

    @Test
    fun ensureMessageSendByServerIsReceivedByClient() = Promise<Unit> { resolve, _ ->
        val messageSent = Message("Test", "Hello, WebSockets!")
        mockServer.on("connection") { socket ->
            socket.send(messageSent.stringify())
        }

        ChatClient(fakeURL).apply {
            start()
            receive {
                assertEquals(messageSent, it)
                mockServer.stop()
                resolve(Unit)
            }
        }
    }

    @Test
    fun ensureFailureEventIsReceivedByClient() = Promise<Unit> { resolve, _ ->
        mockServer.on("connection") {
            mockServer.simulate("error")
        }

        ChatClient(fakeURL).apply {
            onFailure {
                assertEquals(IOException::class, it::class)
                assertEquals("error", it.message)
                mockServer.stop()
                resolve(Unit)
            }
            start()
        }
    }
}