package com.audacoding.pasarlaut.view.user.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentHistroyDetailBottomSheetBinding
import com.audacoding.pasarlaut.millisToDate
import com.audacoding.pasarlaut.models.Order
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HistoryDetailBottomSheet(
    private val order: Order
) : BottomSheetDialogFragment() {
    private var _binding: FragmentHistroyDetailBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistroyDetailBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            Glide.with(requireContext()).load(order.imgUrl).into(ivImage)

            tvName.text = order.productName
            tvAddress.text = order.address
            tvQty.text = order.qty
            tvPrice.text = "Rp${order.price}"
            tvDate.text = millisToDate(order.orderDate, "dd/MM/yyyy")
        }
    }

    companion object {
        const val TAG = "historyDetailBottomSheet"
    }
}