package com.audacoding.pasarlaut.view.nelayan.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.databinding.ActivityNelayanChatBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.view.nelayan.chatDetail.NelayanChatDetailActivity
import com.audacoding.pasarlaut.visible

class NelayanChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNelayanChatBinding
    private lateinit var adapter: NelayanChatAdapter

    private val viewModel by viewModels<NelayanChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNelayanChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getChats()
        initListeners()

        adapter = NelayanChatAdapter { product, name ->
            val i = Intent(this, NelayanChatDetailActivity::class.java)
            i.putExtra("product", product)
            i.putExtra("name", name)

            startActivity(i)
        }

        binding.apply {
            ivBack.setOnClickListener { finish() }

            rvChat.adapter = adapter
            rvChat.layoutManager = LinearLayoutManager(this@NelayanChatActivity)
            rvChat.addItemDecoration(DividerItemDecoration(this@NelayanChatActivity, LinearLayoutManager.VERTICAL))
        }
    }

    private fun initListeners() {
        viewModel.onLoading.observe(this) {
            if(it) {
                binding.loading.loadingScreen.visible()
            } else {
                binding.loading.loadingScreen.gone()
            }
        }
        viewModel.onError.observe(this) {
            if(it.isNotEmpty()) {
                showToast(this, it)
            }
        }
        viewModel.chats.observe(this) {
            adapter.submitList(it)
        }
    }
}