import com.github.felipehjcosta.chatapp.client.ChatClient
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

private const val TEST_TIMEOUT = 5000L
private const val WAIT_THREAD = 500L
private const val WEBSOCKET_CONNECTION_CODE = 11

class WebSocketClientTest {

    private val mockWebServer = MockWebServer()

    private val mockServerWebSocketListener = spyk<WebSocketListener>()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureAStartedConnectionIsOpenedByClient() {

        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}")
        chatClient.start()

        val webSocketSlot = slot<WebSocket>()
        val responseSlot = slot<Response>()
        verify { mockServerWebSocketListener.onOpen(capture(webSocketSlot), capture(responseSlot)) }
        assertEquals(WEBSOCKET_CONNECTION_CODE, responseSlot.captured.code())
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureMessageSendByServerIsReceivedByClient() {

        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}").apply { start() }

        val mockOnReceive = mockk<(Message) -> Unit>()
        chatClient.receive(mockOnReceive)

        Thread.sleep(WAIT_THREAD)

        val webSocketSlot = slot<WebSocket>()
        verify { mockServerWebSocketListener.onOpen(capture(webSocketSlot), any()) }

        val message = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        webSocketSlot.captured.send(message)

        Thread.sleep(WAIT_THREAD)

        val receivedMessageSlot = slot<String>()
        verify { mockOnReceive(capture(receivedMessageSlot)) }
        assertEquals(message, receivedMessageSlot.captured)
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureSendMessageIsReceivedByServer() {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}").apply { start() }

        Thread.sleep(WAIT_THREAD)

        verify { mockServerWebSocketListener.onOpen(any(), any()) }

        val message = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        chatClient.send(message)

        Thread.sleep(WAIT_THREAD)

        verify { mockServerWebSocketListener.onMessage(any(), message) }
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureFailureEventIsReceivedByClient() {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}").apply { start() }

        Thread.sleep(WAIT_THREAD)

        val mockOnFailure = mockk<(Throwable) -> Unit>()
        chatClient.onFailure(mockOnFailure)

        mockWebServer.shutdown()

        verify { mockOnFailure(any()) }
    }
}
