package screen.chat

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import navigation.Component

class ChatScreenComponent(
    private val componentContext: ComponentContext,
    private val userName: String,
    private val onGoBack: () -> Unit
) : Component, ComponentContext by componentContext {

    @Composable
    override fun render() {
        Chat(
            userName = userName,
            onGoBack = onGoBack
        )
    }
}