package screen.welcome

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import navigation.Component

class WelcomeScreenComponent(
    private val componentContext: ComponentContext,
    private val onEnter: (userName: String) -> Unit
) : Component, ComponentContext by componentContext {

    @Composable
    override fun render() {
        Welcome(
            onEnter = onEnter
        )
    }
}