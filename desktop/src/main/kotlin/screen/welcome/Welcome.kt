package screen.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Welcome(onEnter: (userName: String) -> Unit) {
    var text by remember { mutableStateOf("") }

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
                onEnter(text)
            }) {
            Text("ENTER")
        }
    }
}