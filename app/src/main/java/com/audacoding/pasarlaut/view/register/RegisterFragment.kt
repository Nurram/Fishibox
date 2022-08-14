package com.audacoding.pasarlaut.view.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentRegisterBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.visible

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        binding.apply {
            btnRegister.setOnClickListener {
                if(etRegisterEmail.text.isNotEmpty() &&
                    etRegisterUsername.text.isNotEmpty() &&
                    etRegisterPassword.text.isNotEmpty()) {
                    register()
                } else {
                    showToast(requireContext(), getString(R.string.field_empty))
                }
            }
            tvRegisterLogin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun register() {
        binding.apply {
            val userType = if(rbRegisterNelayan.isChecked) {
                "nelayan"
            } else {
                "pembeli"
            }

            viewModel.register(
                etRegisterUsername.text.toString(),
                etRegisterEmail.text.toString(),
                etRegisterPassword.text.toString(),
                userType
            )
        }
    }

    private fun initListeners() {
        viewModel.onLoading.observe(viewLifecycleOwner) {
            if(it) {
                binding.loadingScreen.visible()
            } else {
                binding.loadingScreen.gone()
            }
        }
        viewModel.onError.observe(viewLifecycleOwner) {
            showToast(requireContext(), it)
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().popBackStack()
                showToast(requireContext(), getString(R.string.register_success))
            }
        }
    }
}