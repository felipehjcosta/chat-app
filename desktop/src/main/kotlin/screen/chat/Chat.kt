package screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Chat(userName: String, onGoBack: ()->Unit) {
    Column{
        Text("Hello $userName")
        Button(onClick = onGoBack) {
            Text("Go Back")
        }
    }
}