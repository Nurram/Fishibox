package com.audacoding.pasarlaut.view.nelayan.chatDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audacoding.pasarlaut.databinding.ItemListChatDetailBinding
import com.audacoding.pasarlaut.millisToDate
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.visible

class NelayanChatDetailAdapter(
    private val uid: String
): ListAdapter<Chat, NelayanChatDetailAdapter.ChatViewHolder>(DIFF_UTIL) {

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

    inner class ChatViewHolder(
        private val binding: ItemListChatDetailBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.apply {
                if (uid != chat.senderId) {
                    tvReceiver.visible()
                    tvReceiver.text = chat.message
                } else {
                    tvSender.visible()
                    tvSender.text = chat.message
                }

                tvTime.text = millisToDate(chat.sendAt, "HH:mm")
            }
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListChatDetailBinding.inflate(inflater, parent, false)

        return ChatViewHolder(binding)
    }
}