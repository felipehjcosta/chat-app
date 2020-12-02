import com.github.felipehjcosta.chatapp.client.PlatformSocket
import com.github.felipehjcosta.chatapp.client.PlatformSocketListener
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FakePlatformSocket : PlatformSocket("") {

    private lateinit var listener: PlatformSocketListener

    private var isStartCalled: Boolean = false

    private var sentMessage: String? = null

    override fun openSocket(listener: PlatformSocketListener) {
        this.listener = listener
        isStartCalled = true
    }

    fun assertStartWasCalled() = assertTrue { isStartCalled }

    override fun sendMessage(msg: String) {
        this.sentMessage = msg
    }

    fun assetSendMessageWasCalled(msg: String) = assertEquals(msg, sentMessage)

    fun forceCallReceive(msg: String) = listener.onMessage(msg)

    fun forceCallOnFailure(throwable: Throwable) =listener.onFailure(throwable)

    override fun closeSocket(code: Int, reason: String) {
        TODO("FAKE NOT IMPLEMENTED")
    }
}
