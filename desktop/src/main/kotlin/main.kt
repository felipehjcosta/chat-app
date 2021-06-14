import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.logging.LoggerAdapter
import navigation.Root

fun main() {

    Logger {
        loggerAdapter = object : LoggerAdapter {
            override fun info(message: String) {
                println(message)
            }
        }
    }

    ChatInjector {
        baseUrl = "ws://localhost:8089/chat"
    }

    return Window(title = "Compose for Desktop", size = IntSize(300, 300)) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MaterialTheme {
                DesktopTheme {
                    rememberRootComponent(factory = ::Root).render()
                }
            }
        }
    }
}