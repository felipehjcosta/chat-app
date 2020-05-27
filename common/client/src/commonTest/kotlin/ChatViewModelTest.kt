import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.chatapp.stringify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChatViewModelTest {

    private val stubChatClient = FakeWebSocketClient()

    private val chatViewModel = ChatViewModel(stubChatClient)

    @Test
    fun ensureViewModelStartsChatConnection() {
        chatViewModel.start()

        stubChatClient.assertStartWasCalled()
    }

    @Test
    fun ensureViewModelSendsMessage() {
        val message = Message("author", "message")
        chatViewModel.sendMessage(message)

        stubChatClient.assetSendMessageWasCalled(message.stringify())
    }

    @Test
    fun ensureViewModelNotifiesFailure() {
        var receivedShowFailureMessageResult = false

        chatViewModel.showFailureMessage = {
            receivedShowFailureMessageResult = it
        }

        stubChatClient.forceCallOnFailure(Throwable())

        assertTrue { receivedShowFailureMessageResult }
    }

    @Test
    fun ensureViewModelNotifiesReceivedMessage() {
        val message = Message("author", "message")
        var receivedOnChat: Message? = null

        chatViewModel.onChat = {
            receivedOnChat = it
        }

        stubChatClient.forceCallReceive(message.stringify())

        assertEquals(message, receivedOnChat)
    }

}
