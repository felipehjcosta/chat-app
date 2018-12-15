import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatClient
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class ChatClientTest {

    private val mockWebServer = MockWebServer()

    private val mockServerWebSocketListener = spyk<WebSocketListener>()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test(timeout = 5000L)
    fun ensureAStartedConnectionIsOpenedByClient() {

        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}")
        chatClient.start()

        val webSocketSlot = slot<WebSocket>()
        val responseSlot = slot<Response>()
        verify { mockServerWebSocketListener.onOpen(capture(webSocketSlot), capture(responseSlot)) }
        assertEquals(101, responseSlot.captured.code())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test(timeout = 5000L)
    fun ensureMessageSendByServerIsReceivedByClient() {

        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}").apply { start() }

        val mockOnReceive = mockk<(Message) -> Unit>()
        chatClient.receive(mockOnReceive)

        Thread.sleep(500L)

        val webSocketSlot = slot<WebSocket>()
        verify { mockServerWebSocketListener.onOpen(capture(webSocketSlot), any()) }

        val message = Message("Test", "Hello, WebSockets!")
        webSocketSlot.captured.send(JSON.stringify(Message::class.serializer(), message))

        Thread.sleep(500L)

        val receivedMessageSlot = slot<Message>()
        verify { mockOnReceive(capture(receivedMessageSlot)) }
        assertEquals(message, receivedMessageSlot.captured)
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test(timeout = 5000L)
    fun ensureSendMessageIsReceivedByServer() {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}").apply { start() }

        verify { mockServerWebSocketListener.onOpen(any(), any()) }

        val message = Message("Test", "Hello, WebSockets!")
        chatClient.send(message)

        verify { mockServerWebSocketListener.onMessage(any(), JSON.stringify(Message::class.serializer(), message)) }
    }

    @Test(timeout = 5000L)
    fun ensureFailureEventIsReceivedByClient() {
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(mockServerWebSocketListener))
        val chatClient = ChatClient("http://${mockWebServer.hostName}:${mockWebServer.port}").apply { start() }

        val mockOnFailure = mockk<(Throwable) -> Unit>()
        chatClient.onFailure(mockOnFailure)

        mockWebServer.shutdown()

        verify { mockOnFailure(any()) }
    }
}