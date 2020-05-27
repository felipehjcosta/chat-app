import com.github.felipehjcosta.chatapp.client.WebSocketClient
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FakeWebSocketClient : WebSocketClient("") {

    private var isStartCalled: Boolean = false

    private var sentMessage: String? = null

    private var receiveMessageLambda: ((String) -> Unit)? = null

    private var onFailureLambda: ((Throwable) -> Unit)? = null

    override fun start() {
        isStartCalled = true
    }

    fun assertStartWasCalled() = assertTrue { isStartCalled }

    override fun send(webSocketMessage: String) {
        sentMessage = webSocketMessage
    }

    fun assetSendMessageWasCalled(String: String) = assertEquals(String, sentMessage)

    override fun receive(receiveBlock: (String) -> Unit) {
        receiveMessageLambda = receiveBlock
    }

    fun forceCallReceive(String: String) = receiveMessageLambda?.invoke(String)

    override fun onFailure(throwableBlock: (Throwable) -> Unit) {
        onFailureLambda = throwableBlock
    }

    fun forceCallOnFailure(throwable: Throwable) = onFailureLambda?.invoke(throwable)
}
