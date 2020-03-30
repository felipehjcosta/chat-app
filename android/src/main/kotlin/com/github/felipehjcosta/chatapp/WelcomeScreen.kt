package com.github.felipehjcosta.chatapp


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.welcome_screen.go_to_chat as goToChat
import kotlinx.android.synthetic.main.welcome_screen.username_input as usernameInput

class WelcomeScreen : Fragment(R.layout.welcome_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goToChat.setOnClickListener {
            activity?.hideKeyboard()
            val userName = usernameInput.text.toString()
            val action = WelcomeScreenDirections.actionGoToChat(userName)
            findNavController().navigate(action)
        }
    }
}
