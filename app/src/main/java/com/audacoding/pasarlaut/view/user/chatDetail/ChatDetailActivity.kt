package com.audacoding.pasarlaut.view.user.chatDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.databinding.ActivityChatBinding
import com.audacoding.pasarlaut.getStringFromPref
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.visible

class ChatDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatDetailAdapter

    private val viewModel by viewModels<ChatDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<NelayanProduct>("product")!!

        viewModel.getChat(getStringFromPref(this, "username")!!, product.id)
        initListeners()

        adapter = ChatDetailAdapter(viewModel.getUid())
        binding.apply {
            ivBack.setOnClickListener { finish() }
            tvName.text = product.productName

            rvChat.adapter = adapter
            rvChat.layoutManager = LinearLayoutManager(this@ChatDetailActivity)

            ibSend.setOnClickListener {
                val msg = etChat.text.toString()
                etChat.setText("")

                if(msg.isNotEmpty()) {
                    viewModel.sendChat(
                        Chat(
                            id = System.currentTimeMillis(),
                            receiverId = product.uid,
                            nelayanName = product.productName,
                            senderId = viewModel.getUid(),
                            buyerName = getStringFromPref(this@ChatDetailActivity, "username") ?: "-",
                            roomId = "${product.id}.${viewModel.getUid()}",
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
        }
    }
}