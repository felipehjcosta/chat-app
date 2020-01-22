package com.example.android


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.welcome_screen.go_to_chat as goToChat
import kotlinx.android.synthetic.main.welcome_screen.username_input as usernameInput

class WelcomeScreen : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goToChat.setOnClickListener {
            val userName = usernameInput.text.toString()
            val action = WelcomeScreenDirections.actionGoToChat(userName)
            findNavController().navigate(action)
        }
    }
}
