import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatClient
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
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
    fun ensureSendMessageIsReceivedByServer() = Promise<Unit> { resolve, _ ->
        val messageSent = Message("Test", "Hello, WebSockets!")
        mockServer.on("connection") { socket ->
            socket.on("message") { data ->
                assertEquals(messageSent, JSON.parse(Message::class.serializer(), data.toString()))
                mockServer.stop()
                resolve(Unit)
            }
        }

        val chatClient = ChatClient(fakeURL)
        chatClient.send(messageSent)
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test
    fun ensureMessageSendByServerIsReceivedByClient() = Promise<Unit> { resolve, _ ->
        val messageSent = Message("Test", "Hello, WebSockets!")
        mockServer.on("connection") { socket ->
            socket.send(JSON.stringify(Message::class.serializer(), messageSent))
        }

        val chatClient = ChatClient(fakeURL)

        chatClient.receive {
            assertEquals(messageSent, it)
            mockServer.stop()
            resolve(Unit)
        }
    }
}