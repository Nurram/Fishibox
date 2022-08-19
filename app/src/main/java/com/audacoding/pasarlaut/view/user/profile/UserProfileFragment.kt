package com.audacoding.pasarlaut.view.user.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.FragmentUserProfileBinding
import com.audacoding.pasarlaut.models.UserProfile
import com.audacoding.pasarlaut.view.user.history.HistoryActivity
import com.bumptech.glide.Glide

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UserProfileViewModel>()

    private var userProfile: UserProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfile()
        initListeners()

        binding.apply {
            btnNelayanProfileEdit.setOnClickListener {
                if (userProfile != null) {
                    val bottomSheet = UserProfileBottomSheet(userProfile!!)
                    bottomSheet.show(childFragmentManager, UserProfileBottomSheet.TAG)
                }
            }
            btnNelayanProfileHistory.setOnClickListener {
                val i = Intent(requireContext(), HistoryActivity::class.java)
                startActivity(i)
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
            userProfile = it.toObject(UserProfile::class.java)

            binding.apply {
                tvNelayanProfileName.text = it.getString("name") ?: "-"
                tvNelayanProfileUsername.text = it.getString("username") ?: "-"
                tvNelayanProfileAddress.text = it.getString("address") ?: "-"
                tvNelayanProfilePhone.text = it.getString("phone") ?: "-"

                val imgUrl = it.getString("imgUrl")
                if(imgUrl != null) {
                    Glide.with(requireContext()).load(imgUrl).into(ivNelayanProfile)
                }
            }
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            if(it) {
                val i = Intent(requireContext(), MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(i)
            }
        }
    }

    fun setLoading(loading: Boolean) {
        viewModel.setLoading(loading)
    }
}