import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun main() = Window(title = "Compose for Desktop", size = IntSize(300, 300)) {
    var text by remember { mutableStateOf("") }

    MaterialTheme {
        Column(
            Modifier.padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp).fillMaxSize(),
            Arrangement.spacedBy(5.dp),
            Alignment.CenterHorizontally
        ) {
            TextField(modifier = Modifier.align(Alignment.CenterHorizontally),
                value = text,
                onValueChange = {
                    text = it
                },
                label = { Text("Enter your nickname") })
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {

                }) {
                Text("ENTER")
            }
        }
    }
}