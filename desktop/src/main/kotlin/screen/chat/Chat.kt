package screen.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatViewModel

@Composable
fun Chat(chatViewModel: ChatViewModel, userName: String, onGoBack: () -> Unit) {
    var textMessage by remember { mutableStateOf("") }
    var messages: List<Message> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(userName) {
        println(">>> run")

        chatViewModel.run {
            onChat = {
                println(">>> message: $it")
                messages = messages.toMutableList().apply { add(it) }
            }
            showFailureMessage = { }
            start()
        }
    }

    Column(Modifier.fillMaxSize().padding(4.dp),
    Arrangement.spacedBy(8.dp)) {
        ChatListScreen(Modifier.weight(1f), messages)

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(modifier = Modifier.weight(1f),
                value = textMessage,
                onValueChange = {
                    textMessage = it
                },
                label = { Text("Enter your message") })
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    chatViewModel.sendMessage(Message(userName, textMessage))
                    textMessage = ""
                }) {
                Text("Send")
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = onGoBack) {
            Text("Go Back")
        }
    }
}

@Composable
fun ChatListScreen(modifier: Modifier = Modifier, items: List<Message>) {
    LazyColumn(modifier) {
        items(items = items) { item ->
            Text(
                text = "${item.author}: ${item.message}"
            )
        }
    }
}