package com.audacoding.pasarlaut.view.user.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.audacoding.pasarlaut.databinding.ItemListProductUserBinding
import com.audacoding.pasarlaut.models.NelayanProduct
import com.bumptech.glide.Glide

class DashboardAdapter(
    private val context: Context,
    private val onClick: (NelayanProduct) -> Unit
): RecyclerView.Adapter<DashboardAdapter.UserHomeViewHolder>() {

    private val items = arrayListOf<NelayanProduct>()

    inner class UserHomeViewHolder(private val binding: ItemListProductUserBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: NelayanProduct) {
            binding.apply {
                tvItemName.text = product.productName
                tvItemPrice.text = "Rp${product.price}"

                Glide.with(context).load(product.imgUrl).into(ivItemProduct)
                itemView.setOnClickListener { onClick(product) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHomeViewHolder {
        val binding = ItemListProductUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<NelayanProduct>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}