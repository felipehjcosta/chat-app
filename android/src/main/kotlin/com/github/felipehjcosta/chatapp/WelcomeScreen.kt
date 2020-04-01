package com.github.felipehjcosta.chatapp


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.felipehjcosta.chatapp.databinding.WelcomeScreenBinding

class WelcomeScreen : Fragment(R.layout.welcome_screen) {

    private var screenBinding: WelcomeScreenBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenBinding = WelcomeScreenBinding.bind(view).also { binding ->
            binding.goToChat.setOnClickListener {
                activity?.hideKeyboard()
                val userName = binding.usernameInput.text.toString()
                val action = WelcomeScreenDirections.actionGoToChat(userName)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        screenBinding = null
        super.onDestroyView()
    }
}
