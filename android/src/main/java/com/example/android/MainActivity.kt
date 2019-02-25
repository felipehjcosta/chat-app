package com.example.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatClient
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.recyclerviewdsl.onRecyclerView
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.android.synthetic.main.activity_main.message_input as messageInput
import kotlinx.android.synthetic.main.activity_main.messages_recycler_view as messagesRecyclerView
import kotlinx.android.synthetic.main.activity_main.send_button as sendButton
import kotlinx.android.synthetic.main.activity_main.username_input as usernameInput

@ImplicitReflectionSerializer
class MainActivity : AppCompatActivity() {

    private val chatViewModel = ChatViewModel(ChatClient("${BuildConfig.CHAT_URL_SERVER}/chat"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        chatViewModel.onChat = { runOnUiThread { receiveMessage(it) } }

        sendButton.setOnClickListener { sendMessage() }

        chatViewModel.start()
    }

    private fun setupRecyclerView() {
        onRecyclerView(messagesRecyclerView) {
            withLinearLayout()
            bind(android.R.layout.simple_list_item_1) {
                withItems(emptyList<Message>()) {
                    on<TextView>(android.R.id.text1) {
                        it.view?.text = "${it.item?.author}: ${it.item?.message}"
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val author = usernameInput.text.toString()
        val message = messageInput.text.toString()
        chatViewModel.sendMessage(Message(author, message))
        usernameInput.text?.clear()
        messageInput.text?.clear()
    }

    private fun receiveMessage(message: Message) {
        onRecyclerView(messagesRecyclerView) {
            bind(android.R.layout.simple_list_item_1) {
                addExtraItems(listOf(message))
            }
        }
    }
}
