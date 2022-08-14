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
    private val productId: String,
    private val productName: String,
    private val productStock: String,
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

            btnNelayanAdd.setOnClickListener {
                if(!etNelayanAddNama.text.isNullOrEmpty() &&
                    !etNelayanAddStok.text.isNullOrEmpty() &&
                    !etNelayanAddDesc.text.isNullOrEmpty()) {
                    storeProductData(
                        etNelayanAddNama.text.toString(),
                        etNelayanAddStok.text.toString(),
                        etNelayanAddDesc.text.toString()
                    )
                } else {
                    dismiss()
                }
            }
        }
    }

    private fun storeProductData(productName: String, stock: String, desc: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("products").document(productId).update(
            mapOf(
                "productName" to productName,
                "stock" to stock,
                "desc" to desc
            )
        ).addOnSuccessListener {
            dismiss()
        }
            .addOnFailureListener {
                showToast(requireContext(), it.message.toString())
            }
    }

    companion object {
        const val TAG = "editBottomSheet"
    }
}