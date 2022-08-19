package com.audacoding.pasarlaut.view.nelayan.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audacoding.pasarlaut.databinding.ItemListUserChatBinding
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.models.NelayanProduct

class NelayanChatAdapter(
    private val onClick: (NelayanProduct, String) -> Unit
): ListAdapter<Chat, NelayanChatAdapter.UserChatHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class UserChatHolder(
        private val binding: ItemListUserChatBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.apply {
                tvName.text = chat.buyerName
                tvMsg.text = chat.message

                itemView.setOnClickListener { onClick(chat.productDetail, chat.buyerName) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListUserChatBinding.inflate(inflater, parent, false)

        return UserChatHolder(binding)
    }

    override fun onBindViewHolder(holder: UserChatHolder, position: Int) {
        holder.bind(getItem(position))
    }
}