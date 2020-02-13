package com.github.felipehjcosta.chatapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.recyclerviewdsl.onRecyclerView
import kotlinx.android.synthetic.main.chat_screen.message_input as messageInput
import kotlinx.android.synthetic.main.chat_screen.messages_recycler_view as messagesRecyclerView
import kotlinx.android.synthetic.main.chat_screen.send_button as sendButton

class ChatScreen : Fragment(R.layout.chat_screen) {

    private lateinit var userName: String
    private val chatViewModel: ChatViewModel by ChatInjector.viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readArguments()

        setupRecyclerView()

        chatViewModel.onChat = { activity?.runOnUiThread { receiveMessage(it) } }

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

    private fun readArguments() = arguments?.let {
        val passedArguments = ChatScreenArgs.fromBundle(it)
        userName = passedArguments.username
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
        val message = messageInput.text.toString()
        chatViewModel.sendMessage(Message(userName, message))
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
        view?.findViewById<View>(android.R.id.content)?.run {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}
