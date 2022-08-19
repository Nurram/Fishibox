package com.audacoding.pasarlaut.view.nelayan.chatDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.databinding.ActivityNelayanChatDetailBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.visible

class NelayanChatDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNelayanChatDetailBinding
    private lateinit var adapter: NelayanChatDetailAdapter

    private val viewModel by viewModels<NelayanChatDetailViewModel>()
    private val chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val product = intent.getParcelableExtra<NelayanProduct>("product")!!
        val name = intent.getStringExtra("name")!!

        binding = ActivityNelayanChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getChat(product.id)
        initListeners()

        adapter = NelayanChatDetailAdapter(viewModel.getUid())
        binding.apply {
            ivBack.setOnClickListener { finish() }
            tvName.text = name

            rvChat.adapter = adapter
            rvChat.layoutManager = LinearLayoutManager(this@NelayanChatDetailActivity)

            ibSend.setOnClickListener {
                val msg = etChat.text.toString()
                etChat.setText("")

                if(msg.isNotEmpty()) {
                    viewModel.sendChat(
                        Chat(
                            id = System.currentTimeMillis(),
                            receiverId = chats.first().senderId,
                            nelayanName = product.productName,
                            senderId = viewModel.getUid(),
                            buyerName = chats.first().buyerName,
                            roomId = "${product.id}.${chats.first().senderId}",
                            message = msg,
                            sendAt = System.currentTimeMillis(),
                            productDetail = product
                        )
                    )
                }
            }
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

            chats.clear()
            chats.addAll(it)
        }
    }
}