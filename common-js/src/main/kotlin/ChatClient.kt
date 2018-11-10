import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket

actual class ChatClient actual constructor(url: String) {

    private val socket: WebSocket = WebSocket(url)

    actual fun send(message: Message) {
        socket.send(JSON.stringify(message))
    }

    actual fun receive(receiveBlock: (Message) -> Unit) {
        socket.onmessage = {
            println("on message event: $it")
            val event = it
            when (event) {
                is MessageEvent -> receiveBlock(JSON.parse(event.data.toString()))
            }
        }
    }
}