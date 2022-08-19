package com.audacoding.pasarlaut.view.user.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.ItemListProductFavBinding
import com.audacoding.pasarlaut.databinding.ItemListProductHistoryBinding
import com.audacoding.pasarlaut.millisToDate
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.models.Order
import com.bumptech.glide.Glide

class HistoryAdapter(
    private val context: Context,
    private val onClick: (Order) -> Unit
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val items = arrayListOf<Order>()

    inner class HistoryViewHolder(private val binding: ItemListProductHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            binding.apply {
                tvItemName.text = order.productName
                tvItemQty.text = "Qty: ${order.qty}"
                tvItemPrice.text = "Rp${order.price}"
                tvItemDate.text = millisToDate(order.orderDate, "dd MMMM yyyy")

                Glide.with(context).load(order.imgUrl).into(ivItemProduct)
                itemView.setOnClickListener { onClick(order) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemListProductHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<Order>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}