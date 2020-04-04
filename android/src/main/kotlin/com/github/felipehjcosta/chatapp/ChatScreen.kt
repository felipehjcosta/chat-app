package com.github.felipehjcosta.chatapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.chatapp.databinding.ChatScreenBinding
import com.github.felipehjcosta.recyclerviewdsl.onRecyclerView

class ChatScreen : Fragment(R.layout.chat_screen) {

    private var screenBinding: ChatScreenBinding? = null

    private lateinit var userName: String

    private val chatViewModel: ChatViewModel by ChatInjector.viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readArguments()

        chatViewModel.onChat = { receiveMessage(it) }

        screenBinding = ChatScreenBinding.bind(view).also {
            setupRecyclerView(it.messagesRecyclerView)
            it.sendButton.setOnClickListener { sendMessage() }
            it.messageInput.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEND -> {
                        sendMessage()
                        true
                    }
                    else -> false
                }
            }
        }


        chatViewModel.start()
    }

    private fun readArguments() = arguments?.let {
        val passedArguments = ChatScreenArgs.fromBundle(it)
        userName = passedArguments.username
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        onRecyclerView(recyclerView) {
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
        screenBinding?.let {
            val message = it.messageInput.text.toString()
            chatViewModel.sendMessage(Message(userName, message))
            it.messageInput.text?.clear()
        }
    }

    private fun receiveMessage(message: Message) {
        screenBinding?.let {
            onRecyclerView(it.messagesRecyclerView) {
                bind(android.R.layout.simple_list_item_1) {
                    addExtraItems(listOf(message))
                }
            }
            it.messagesRecyclerView.scheduleScrollToEnd()
        }
    }

    override fun onDestroyView() {
        screenBinding = null
        super.onDestroyView()
    }
}
