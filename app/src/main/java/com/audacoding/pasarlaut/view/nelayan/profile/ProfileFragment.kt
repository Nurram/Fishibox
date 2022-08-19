package com.audacoding.pasarlaut.view.nelayan.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentProfileBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.models.UserProfile
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.view.user.profile.UserProfileBottomSheet
import com.audacoding.pasarlaut.view.user.profile.UserProfileViewModel
import com.audacoding.pasarlaut.visible
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UserProfileViewModel>()

    private var userProfile: UserProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfile()
        initListeners()

        binding.apply {
            btnNelayanProfileEdit.setOnClickListener {
                if(userProfile != null) {
                    val bottomSheet = UserProfileBottomSheet(userProfile!!)
                    bottomSheet.show(childFragmentManager, UserProfileBottomSheet.TAG)
                }
            }
            btnNelayanLogout.setOnClickListener { viewModel.signOut() }
        }
    }

    private fun initListeners() {
        viewModel.onLoading.observe(viewLifecycleOwner) {
            if(it) {
                binding.loading.loadingScreen.visible()
            } else {
                binding.loading.loadingScreen.gone()
            }
        }
        viewModel.onError.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                showToast(requireContext(), it)
            }
        }
        viewModel.userSnapshot.observe(viewLifecycleOwner) {
            binding.apply {
                tvNelayanProfileName.text = it.getString("name") ?: "-"
                tvNelayanProfileUsername.text = it.getString("username") ?: "-"
                tvNelayanProfileAddress.text = it.getString("address") ?: "-"
                tvNelayanProfilePhone.text = it.getString("phone") ?: "-"

                val imgUrl = it.getString("imgUrl")
                if(imgUrl != null) {
                    Glide.with(requireContext()).load(imgUrl).into(ivNelayanProfile)
                }

                userProfile = UserProfile(
                    it.getString("name"),
                    it.getString("username")!!,
                    it.getString("address"),
                    it.getString("phone"),
                    it.getString("imgUrl")
                )
            }
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }
    }
}