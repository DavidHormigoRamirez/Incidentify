package com.alaturing.incidentify.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alaturing.incidentify.R
import com.alaturing.incidentify.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel:LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val identifier = binding.identifierInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.login(identifier,password)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when(uiState) {
                        is LoginUiState.Error -> TODO()
                        LoginUiState.Initial -> {
                            binding.loginProgressIndicator.isVisible = false
                            }
                        LoginUiState.LoggedIn -> {
                            binding.loginProgressIndicator.isVisible = false
                        }
                        LoginUiState.LoggingIn -> { binding.loginProgressIndicator.isVisible = true

                            }
                    }
                    }
            }
        }
    }
}