package screen.chat

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import navigation.Component

class ChatScreenComponent(
    private val componentContext: ComponentContext,
    private val chatViewModel: ChatViewModel,
    private val userName: String,
    private val onGoBack: () -> Unit
) : Component, ComponentContext by componentContext {

    @Composable
    override fun render() {
        Chat(
            chatViewModel = chatViewModel,
            userName = userName,
            onGoBack = onGoBack
        )
    }
}