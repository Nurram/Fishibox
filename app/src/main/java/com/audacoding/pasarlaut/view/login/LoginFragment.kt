package com.audacoding.pasarlaut.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentLoginBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.visible

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
        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if(it) {
                moveToHome()
            }
        }
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
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            if(it) {
                moveToHome()
            }
        }
    }

    private fun moveToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_nelayanHomeFragment)
    }
}