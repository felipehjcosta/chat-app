import com.github.felipehjcosta.chatapp.client.EmptyPlatformSocketListener
import com.github.felipehjcosta.chatapp.client.PlatformSocket
import com.github.felipehjcosta.chatapp.client.PlatformSocketListener
import org.w3c.dom.WebSocket
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformSocketTest {

    private val fakeURL = "ws://localhost:8089"

    private val mockServer = Server(fakeURL)

    @Test
    fun ensureAStartedConnectionIsOpenedByClient() = Promise<Unit> { resolve, _ ->
        mockServer.on("connection") { socket ->
            assertEquals(WebSocket.OPEN, socket.readyState)
            mockServer.stop()
            resolve(Unit)
        }

        PlatformSocket(fakeURL).openSocket(EmptyPlatformSocketListener)
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

        PlatformSocket(fakeURL).apply {
            openSocket(EmptyPlatformSocketListener)
            sendMessage(messageSent)
        }
    }

    @Test
    fun ensureMessageSendByServerIsReceivedByClient() = Promise<Unit> { resolve, _ ->
        val messageSent = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        mockServer.on("connection") { socket ->
            socket.send(messageSent)
        }

        PlatformSocket(fakeURL).openSocket(object :PlatformSocketListener by EmptyPlatformSocketListener {
            override fun onMessage(msg: String) {
                assertEquals(messageSent, msg)
                mockServer.stop()
                resolve(Unit)
            }
        })
    }

    @Test
    fun ensureFailureEventIsReceivedByClient() = Promise<Unit> { resolve, _ ->
        mockServer.on("connection") {
            mockServer.simulate("error")
        }

        PlatformSocket(fakeURL).openSocket(object :PlatformSocketListener by EmptyPlatformSocketListener {
            override fun onFailure(t: Throwable) {
                assertEquals(Error::class, t::class)
                assertEquals("error", t.message)
                mockServer.stop()
                resolve(Unit)
            }
        })
    }
}
