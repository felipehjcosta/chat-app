import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import navigation.Root

fun main() = Window(title = "Compose for Desktop", size = IntSize(300, 300)) {
    Surface(modifier = Modifier.fillMaxSize()) {
        MaterialTheme {
            DesktopTheme {
                rememberRootComponent(factory = ::Root).render()
            }
        }
    }
}