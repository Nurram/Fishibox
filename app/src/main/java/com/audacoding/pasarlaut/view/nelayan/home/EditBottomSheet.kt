package com.audacoding.pasarlaut.view.nelayan.home

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentAddBottomSheetBinding
import com.audacoding.pasarlaut.databinding.FragmentEditBottomSheetBinding
import com.audacoding.pasarlaut.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditBottomSheet(
    private val productId: Long,
    private val productName: String,
    private val productStock: String,
    private val productPrice: String,
    private val productDesc: String
) : BottomSheetDialogFragment() {
    private var _binding: FragmentEditBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etNelayanAddNama.setText(productName)
            etNelayanAddStok.setText(productStock)
            etNelayanAddDesc.setText(productDesc)
            etNelayanAddPrice.setText(productPrice)

            btnNelayanAdd.setOnClickListener {
                showToast(requireContext(), "Silahkan tunggu")

                if(!etNelayanAddNama.text.isNullOrEmpty() &&
                    !etNelayanAddStok.text.isNullOrEmpty() &&
                    !etNelayanAddDesc.text.isNullOrEmpty() &&
                    !etNelayanAddPrice.text.isNullOrEmpty() &&
                    etNelayanAddPrice.text.toString().toInt() > 0) {
                    storeProductData(
                        etNelayanAddNama.text.toString(),
                        etNelayanAddStok.text.toString(),
                        etNelayanAddDesc.text.toString(),
                        etNelayanAddPrice.text.toString()
                    )
                } else {
                    showToast(requireContext(), "Field masih ada yang kosong!")
                }
            }
        }
    }

    private fun storeProductData(productName: String, stock: String, desc: String, price: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("products").whereEqualTo("id", productId)
            .get().addOnSuccessListener {
                firestore.collection("products").document(it.documents.first().id).update(
                mapOf(
                    "productName" to productName,
                    "stock" to stock,
                    "desc" to desc,
                    "price" to price
                )
            ).addOnSuccessListener {
                    showToast(requireContext(), "Data berhasil diubah!")
                    dismiss()
                }
        }
            .addOnFailureListener {
                showToast(requireContext(), it.message.toString())
            }
    }

    companion object {
        const val TAG = "editBottomSheet"
    }
}