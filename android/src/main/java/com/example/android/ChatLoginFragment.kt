package com.example.android


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.chat_login_fragment.go_to_chat as goToChat
import kotlinx.android.synthetic.main.chat_login_fragment.username_input as usernameInput

class ChatLoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goToChat.setOnClickListener {
            val userName = usernameInput.text.toString()
            val action = ChatLoginFragmentDirections.actionGoToChat(userName)
            findNavController().navigate(action)
        }
    }
}
