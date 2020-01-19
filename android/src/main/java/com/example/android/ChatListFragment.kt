package com.example.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.client.ChatViewModel
import com.github.felipehjcosta.recyclerviewdsl.onRecyclerView
import kotlinx.android.synthetic.main.chat_list_fragment.message_input as messageInput
import kotlinx.android.synthetic.main.chat_list_fragment.messages_recycler_view as messagesRecyclerView
import kotlinx.android.synthetic.main.chat_list_fragment.send_button as sendButton

class ChatListFragment : Fragment() {

    private lateinit var userName: String
    private val chatViewModel: ChatViewModel by ChatInjector.viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chat_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val passedArguments = ChatListFragmentArgs.fromBundle(it)
            userName = passedArguments.username
        }

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
