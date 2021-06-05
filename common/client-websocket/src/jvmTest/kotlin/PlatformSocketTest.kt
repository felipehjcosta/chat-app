import com.github.felipehjcosta.chatapp.client.PlatformSocket
import com.github.felipehjcosta.chatapp.client.PlatformSocketListener
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Rule
import org.junit.Test
import kotlin.concurrent.thread
import kotlin.test.assertEquals

private const val TEST_TIMEOUT = 5000L
private const val WAIT_THREAD = 500L
private const val WEBSOCKET_CONNECTION_CODE = 101

@ExperimentalCoroutinesApi
class PlatformSocketTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    val mockPlatformSocketListener = mockk<PlatformSocketListener>(relaxed = true)

    private val mockWebServer = MockWebServer()

    private val mockServerWebSocketListener = spyk<WebSocketListener>()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureAStartedConnectionIsOpenedByClient() = runBlockingTest {

        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        PlatformSocket("http://${mockWebServer.hostName}:${mockWebServer.port}")
            .openSocket(mockPlatformSocketListener)

        val webSocketSlot = slot<WebSocket>()
        val responseSlot = slot<Response>()
        verify { mockServerWebSocketListener.onOpen(capture(webSocketSlot), capture(responseSlot)) }
        assertEquals(WEBSOCKET_CONNECTION_CODE, responseSlot.captured.code)
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureMessageSendByServerIsReceivedByClient() = runBlockingTest {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        PlatformSocket("http://${mockWebServer.hostName}:${mockWebServer.port}")
            .openSocket(mockPlatformSocketListener)

        Thread.sleep(WAIT_THREAD)
        val webSocketSlot = slot<WebSocket>()
        verify { mockServerWebSocketListener.onOpen(capture(webSocketSlot), any()) }

        val message = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        webSocketSlot.captured.send(message)

        Thread.sleep(WAIT_THREAD)

        val receivedMessageSlot = slot<String>()
        verify { mockPlatformSocketListener.onMessage(capture(receivedMessageSlot)) }
        assertEquals(message, receivedMessageSlot.captured)
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureSendMessageIsReceivedByServer() {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val platformSocket = PlatformSocket("http://${mockWebServer.hostName}:${mockWebServer.port}").apply {
            openSocket(mockPlatformSocketListener)
        }

        Thread.sleep(WAIT_THREAD)

        verify { mockServerWebSocketListener.onOpen(any(), any()) }

        val message = "{\"author\": \"Test\", \"message\":\"Hello, WebSockets!\"}"
        platformSocket.sendMessage(message)

        Thread.sleep(WAIT_THREAD)

        verify { mockServerWebSocketListener.onMessage(any(), message) }
    }

    @Test(timeout = TEST_TIMEOUT)
    fun ensureFailureEventIsReceivedByClient() {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        PlatformSocket("http://${mockWebServer.hostName}:${mockWebServer.port}")
            .openSocket(mockPlatformSocketListener)

        Thread.sleep(WAIT_THREAD)

        mockWebServer.shutdown()

        verify { mockPlatformSocketListener.onFailure(any()) }
    }
}
