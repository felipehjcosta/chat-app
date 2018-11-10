expect class ChatClient(url: String) {
    fun send(message: Message)
    fun receive(receiveBlock: (Message) -> Unit)
}