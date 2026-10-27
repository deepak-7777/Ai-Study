package com.aistudyos.app.presentation.auth.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aistudyos.app.R
import com.aistudyos.app.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
    private var _b: FragmentForgotPasswordBinding? = null
    private val b get() = _b!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentForgotPasswordBinding.bind(view)
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
