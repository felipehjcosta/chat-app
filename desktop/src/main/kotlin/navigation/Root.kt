package navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.github.felipehjcosta.chatapp.client.ChatInjector
import screen.chat.ChatScreenComponent
import screen.welcome.WelcomeScreenComponent

class Root(componentContext: ComponentContext) : Component, ComponentContext by componentContext {

    private val router = router<ScreenConfiguration, Component>(
        initialConfiguration = { ScreenConfiguration.Welcome },
        childFactory = ::createScreenComponent,
        configurationClass = ScreenConfiguration::class
    )

    /**
     * Factory function to create screen from given ScreenConfig
     */
    private fun createScreenComponent(
        configuration: ScreenConfiguration,
        componentContext: ComponentContext
    ): Component {
        return when (configuration) {

            is ScreenConfiguration.Welcome -> WelcomeScreenComponent(
                componentContext,
                ::onEnter
            )

            is ScreenConfiguration.Chat -> ChatScreenComponent(
                componentContext,
                chatViewModel = ChatInjector.viewModel().value,
                configuration.userName,
                ::onGoBackClicked
            )
        }
    }

    private fun onEnter(userName: String) {
        router.push(ScreenConfiguration.Chat(userName))
    }

    private fun onGoBackClicked() {
        router.pop()
    }

    @Composable
    override fun render() {
        Children(routerState = router.state) {
            it.instance.render()
        }
    }
}