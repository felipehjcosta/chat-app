import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.WebSocketClient
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FakeWebSocketClient : WebSocketClient("") {

    private var isStartCalled: Boolean = false

    private var sentMessage: Message? = null

    private var receiveMessageLambda: ((Message) -> Unit)? = null

    private var onFailureLambda: ((Throwable) -> Unit)? = null

    override fun start() {
        isStartCalled = true
    }

    fun assertStartWasCalled() = assertTrue { isStartCalled }

    override fun send(message: Message) {
        sentMessage = message
    }

    fun assetSendMessageWasCalled(message: Message) = assertEquals(message, sentMessage)

    override fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessageLambda = receiveBlock
    }

    fun forceCallReceive(message: Message) = receiveMessageLambda?.invoke(message)

    override fun onFailure(throwableBlock: (Throwable) -> Unit) {
        onFailureLambda = throwableBlock
    }

    fun forceCallOnFailure(throwable: Throwable) = onFailureLambda?.invoke(throwable)
}
