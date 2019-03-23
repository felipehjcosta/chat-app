package com.example.android

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.recyclerviewdsl.onRecyclerView
import kotlinx.android.synthetic.main.activity_main.message_input as messageInput
import kotlinx.android.synthetic.main.activity_main.messages_recycler_view as messagesRecyclerView
import kotlinx.android.synthetic.main.activity_main.send_button as sendButton
import kotlinx.android.synthetic.main.activity_main.username_input as usernameInput


class MainActivity : AppCompatActivity() {

    private val chatViewModel: ChatViewModel by ChatInjector.viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        chatViewModel.onChat = { runOnUiThread { receiveMessage(it) } }

        sendButton.setOnClickListener { sendMessage() }
        messageInput.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }

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
        hideKeyboard()
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

    private fun hideKeyboard() {
        findViewById<View>(android.R.id.content)?.run {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}
