package com.aistudyos.app.presentation.auth.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aistudyos.app.R
import com.aistudyos.app.core.common.extensions.showToast
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.databinding.FragmentLoginBinding
import com.aistudyos.app.presentation.auth.AuthActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    // Result launcher
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken

                if (idToken != null) {

                    Log.d("GOOGLE", "Callback reached")
                    Log.d("GOOGLE", "Email: ${account.email}")
                    Log.d("GOOGLE", "ID TOKEN: $idToken")

                    viewModel.googleLogin(idToken)

                    Log.d("GOOGLE", "googleLogin() called")
                } else {
                    showToast("Google token null")
                }

            } catch (e: Exception) {
                Log.e("GOOGLE", "Google sign-in failed", e)
                showToast("Google sign-in failed: ${e.message}")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        // 🔐 Google config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Email login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        // 🔥 Google login click
        binding.btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot)
        }

        observeState()

    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    binding.progressBar.isVisible = state is UiState.Loading
                    binding.btnLogin.isEnabled = state !is UiState.Loading

                    when (state) {
                        is UiState.Success -> {
                            (activity as? AuthActivity)?.goToMain()
                        }
                        is UiState.Error -> showToast(state.message)
                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}