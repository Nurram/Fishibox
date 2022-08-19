package com.audacoding.pasarlaut.view.user.chat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.FragmentUserChatBinding
import com.audacoding.pasarlaut.view.user.chatDetail.ChatDetailActivity

class UserChatFragment : Fragment() {
    private lateinit var binding: FragmentUserChatBinding
    private lateinit var adapter: UserChatAdapter

    private val viewModel by viewModels<UserChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChats(getStringFromPref(requireContext(), "username")!!)
        initListeners()

        adapter = UserChatAdapter {
            val i = Intent(requireContext(), ChatDetailActivity::class.java)
            i.putExtra("product", it)

            startActivity(i)
        }

        binding.apply {
            rvChat.adapter = adapter
            rvChat.layoutManager = LinearLayoutManager(requireContext())
            rvChat.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
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
        viewModel.chats.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}