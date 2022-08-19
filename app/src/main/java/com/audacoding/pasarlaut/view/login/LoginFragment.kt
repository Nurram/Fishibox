package com.audacoding.pasarlaut.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.FragmentLoginBinding
import com.audacoding.pasarlaut.view.forgot.ForgotPasswordActivity

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        viewModel.checkUserLoggedIn()

        binding.apply {
            btnLogin.setOnClickListener {
                if (
                    etLoginUsername.text.isNotEmpty() &&
                    etLoginPassword.text.isNotEmpty()
                ) {
                    login()
                }
            }
            tvLoginForgot.setOnClickListener {
                val i = Intent(requireContext(), ForgotPasswordActivity::class.java)
                startActivity(i)
            }
            tvLoginRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }


    private fun login() {
        binding.apply {
            viewModel.login(
                etLoginUsername.text.toString(),
                etLoginPassword.text.toString()
            )
        }
    }

    private fun initListener() {
        viewModel.onLoading.observe(viewLifecycleOwner) {
            if(it) {
                binding.loading.loadingScreen.visible()
            } else {
                binding.loading.loadingScreen.gone()
            }
        }
        viewModel.onError.observe(viewLifecycleOwner) {
            showToast(requireContext(), it)
        }
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            val loginSuccess: Boolean = it["loginSuccess"] as Boolean
            val userType: String = it["userType"] as String
            val username: String = it["username"] as String

           storeStringToPref(requireContext(), "userType", userType)
            storeStringToPref(requireContext(), "username", username)
            if(loginSuccess) {
                if(userType == "nelayan") {
                    moveToHome()
                } else {
                    moveToUserHome()
                }
            }
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            if(it) {
                val userType = getStringFromPref(requireContext(), "userType")!!

                if(userType == "nelayan") {
                    moveToHome()
                } else {
                    moveToUserHome()
                }
            }
        }
    }

    private fun moveToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_nelayanHomeFragment)
    }

    private fun moveToUserHome() {
        findNavController().navigate(R.id.action_loginFragment_to_userHomeFragment)
    }
}