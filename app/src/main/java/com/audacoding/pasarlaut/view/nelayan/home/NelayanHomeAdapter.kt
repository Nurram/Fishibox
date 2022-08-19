package com.audacoding.pasarlaut.view.nelayan.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.ItemListProductBinding
import com.audacoding.pasarlaut.models.NelayanProduct
import com.bumptech.glide.Glide

class NelayanHomeAdapter(
    private val context: Context,
    private val onClick: (NelayanProduct) -> Unit
): RecyclerView.Adapter<NelayanHomeAdapter.NelayanHomeViewHolder>() {

    private val items = arrayListOf<NelayanProduct>()

    inner class NelayanHomeViewHolder(private val binding: ItemListProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: NelayanProduct) {
            binding.apply {
                tvItemName.text = product.productName
                tvItemStock.text = context.getString(R.string.stock, product.stock)

                Glide.with(context).load(product.imgUrl).into(ivItemProduct)
                itemView.setOnClickListener { onClick(product) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NelayanHomeViewHolder {
        val binding = ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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