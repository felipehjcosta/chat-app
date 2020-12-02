import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.chatapp.stringify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChatViewModelTest {

    private val fakePlatformSocket = FakePlatformSocket()

    private val chatViewModel = ChatViewModel(fakePlatformSocket)

    @Test
    fun ensureViewModelStartsChatConnection() {
        chatViewModel.start()

        fakePlatformSocket.assertStartWasCalled()
    }

    @Test
    fun ensureViewModelSendsMessage() {
        val message = Message("author", "message")
        chatViewModel.sendMessage(message)

        fakePlatformSocket.assetSendMessageWasCalled(message.stringify())
    }

    @Test
    fun ensureViewModelNotifiesFailure() {
        var receivedShowFailureMessageResult = false

        chatViewModel.showFailureMessage = {
            receivedShowFailureMessageResult = it
        }

        chatViewModel.start()

        fakePlatformSocket.forceCallOnFailure(Throwable())

        assertTrue { receivedShowFailureMessageResult }
    }

    @Test
    fun ensureViewModelNotifiesReceivedMessage() {
        val message = Message("author", "message")
        var receivedOnChat: Message? = null

        chatViewModel.onChat = {
            receivedOnChat = it
        }

        chatViewModel.start()

        fakePlatformSocket.forceCallReceive(message.stringify())

        assertEquals(message, receivedOnChat)
    }

}
