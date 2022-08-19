package com.audacoding.pasarlaut.view.user.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.ItemListProductFavBinding
import com.audacoding.pasarlaut.models.NelayanProduct
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context,
    private val onClick: (NelayanProduct, Int) -> Unit
): RecyclerView.Adapter<CartAdapter.NelayanHomeViewHolder>() {

    private val items = arrayListOf<NelayanProduct>()

    inner class NelayanHomeViewHolder(private val binding: ItemListProductFavBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: NelayanProduct) {
            binding.apply {
                tvItemName.text = product.productName
                tvItemStock.text = "Rp${product.price}"
                ivRemove.setOnClickListener { onClick(product, adapterPosition) }

                Glide.with(context).load(product.imgUrl).into(ivItemProduct)
                itemView.setOnClickListener { onClick(product, -1) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NelayanHomeViewHolder {
        val binding = ItemListProductFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NelayanHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NelayanHomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<NelayanProduct>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}