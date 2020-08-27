package com.github.felipehjcosta.chatapp


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.felipehjcosta.chatapp.databinding.WelcomeScreenBinding

class WelcomeScreen : Fragment(R.layout.welcome_screen) {

    private val binding: WelcomeScreenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            goToChat.setOnClickListener {
                activity?.hideKeyboard()
                val userName = usernameInput.text.toString()
                val action = WelcomeScreenDirections.actionGoToChat(userName)
                findNavController().navigate(action)
            }
        }
    }

}
