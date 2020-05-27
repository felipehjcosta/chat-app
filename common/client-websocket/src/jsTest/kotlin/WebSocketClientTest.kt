import com.github.felipehjcosta.chatapp.client.WebSocketClient
import org.w3c.dom.WebSocket
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

class WebSocketClientTest {

    private val fakeURL = "ws://localhost:8089"

    private val mockServer = Server(fakeURL)

    @Test
    fun ensureAStartedConnectionIsOpenedByClient() = Promise<Unit> { resolve, _ ->
        mockServer.on("connection") { socket ->
            assertEquals(WebSocket.OPEN, socket.readyState)
            mockServer.stop()
            resolve(Unit)
        }

        WebSocketClient(fakeURL).apply { start() }
    }

    @Test
    fun ensureSendMessageIsReceivedByServer() = Promise<Unit> { resolve, _ ->
        val messageSent = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        mockServer.on("connection") { socket ->
            socket.on("message") { data ->
                assertEquals(messageSent, data.toString())
                mockServer.stop()
                resolve(Unit)
            }
        }

        WebSocketClient(fakeURL).apply {
            start()
            send(messageSent)
        }
    }

    @Test
    fun ensureMessageSendByServerIsReceivedByClient() = Promise<Unit> { resolve, _ ->
        val messageSent = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        mockServer.on("connection") { socket ->
            socket.send(messageSent)
        }

        WebSocketClient(fakeURL).apply {
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

        WebSocketClient(fakeURL).apply {
            onFailure {
                assertEquals(Error::class, it::class)
                assertEquals("error", it.message)
                mockServer.stop()
                resolve(Unit)
            }
            start()
        }
    }
}
