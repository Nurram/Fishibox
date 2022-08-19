package com.audacoding.pasarlaut.view.user.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.FragmentUserHomeBinding
import com.audacoding.pasarlaut.view.user.cart.CartFragment
import com.audacoding.pasarlaut.view.user.chat.UserChatFragment
import com.audacoding.pasarlaut.view.user.dashboard.DashboardFragment
import com.audacoding.pasarlaut.view.user.favorite.FavoriteFragment
import com.audacoding.pasarlaut.view.user.profile.UserProfileFragment

class UserHomeFragment : Fragment(R.layout.fragment_user_home) {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveFragment(DashboardFragment())

        _binding = FragmentUserHomeBinding.bind(view)
        binding.apply {
            botnav.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.user_home -> {
                        moveFragment(DashboardFragment())
                        true
                    }
                    R.id.user_profile -> {
                        moveFragment(UserProfileFragment())
                        true
                    }
                    R.id.user_favorite -> {
                        moveFragment(FavoriteFragment())
                        true
                    }
                    R.id.user_cart -> {
                        moveFragment(CartFragment())
                        true
                    }
                    R.id.user_chat -> {
                        moveFragment(UserChatFragment())
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun moveFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.container_user, fragment).commit()
    }
}