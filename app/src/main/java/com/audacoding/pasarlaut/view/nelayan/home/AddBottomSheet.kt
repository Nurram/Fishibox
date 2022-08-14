package com.audacoding.pasarlaut.view.nelayan.home

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentAddBottomSheetBinding
import com.audacoding.pasarlaut.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentAddBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var fileUri: Uri? = null

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                this.fileUri = fileUri
                binding.btnNelayanAddPhoto.text = fileUri.path
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showToast(requireContext(), ImagePicker.getError(data))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnNelayanAdd.setOnClickListener {
                if(!etNelayanAddNama.text.isNullOrEmpty() &&
                    !etNelayanAddStok.text.isNullOrEmpty() &&
                    !etNelayanAddDesc.text.isNullOrEmpty() &&
                    fileUri != null) {
                    addData()
                } else {
                    dismiss()
                }
            }
            btnNelayanAddPhoto.setOnClickListener {
                ImagePicker.with(this@AddBottomSheet)
                    .compress(512)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
        }
    }

    private fun addData() {
        val storage = FirebaseStorage.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userStorageRef = storage.getReference(auth.uid!!)

        binding.apply {
            userStorageRef.child("products").putFile(fileUri!!)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { url ->
                        storeProductData(
                            auth.uid!!,
                            etNelayanAddNama.text.toString(),
                            etNelayanAddStok.text.toString(),
                            etNelayanAddDesc.text.toString(),
                            url)
                    }.addOnFailureListener { e ->
                        showToast(requireContext(), e.message.toString())
                    }
            }.addOnFailureListener { e ->
                    showToast(requireContext(), e.message.toString())
            }
        }
    }

    private fun storeProductData(
        uid: String,
        productName: String,
        stock: String,
        desc: String,
        imgUrl: Uri) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("products").add(
            mapOf(
                "id" to System.currentTimeMillis(),
                "userId" to uid,
                "productName" to productName,
                "stock" to stock,
                "desc" to desc,
                "imgUrl" to imgUrl
            )
        ).addOnSuccessListener {
            dismiss()
        }
            .addOnFailureListener {
                showToast(requireContext(), it.message.toString())
            }
    }

    companion object {
        const val TAG = "addBottomSheet"
    }
}