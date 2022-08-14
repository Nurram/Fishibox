package com.audacoding.pasarlaut.view.nelayan.profile

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
import com.audacoding.pasarlaut.databinding.FragmentProfileBottomSheetBinding
import com.audacoding.pasarlaut.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentProfileBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var fileUri: Uri? = null

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                this.fileUri = fileUri
                binding.btnAddPhoto.text = fileUri.path
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showToast(requireContext(), ImagePicker.getError(data))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnAddPhoto.setOnClickListener {
                ImagePicker.with(this@ProfileBottomSheet)
                    .compress(512)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            btnAdd.setOnClickListener {
                if(
                    !etName.text.isNullOrEmpty() &&
                    !etAddress.text.isNullOrEmpty() &&
                    !etPhone.text.isNullOrEmpty() &&
                    fileUri != null
                ) {
                    saveData()
                } else {
                    dismiss()
                }
            }
        }
    }

    private fun saveData() {
        storage.getReference("users").putFile(fileUri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { url ->
                    storeToDb(url)
                }
            }.addOnFailureListener {
                showToast(requireContext(), it.message.toString())
            }
    }

    private fun storeToDb(imgUrl: Uri) {
        val uid = auth.uid!!
        firestore.collection("users").document(uid).update(
            mapOf(
                "name" to binding.etName.text.toString(),
                "address" to binding.etAddress.text.toString(),
                "phone" to binding.etPhone.text.toString(),
                "imgUrl" to imgUrl
            )
        ).addOnSuccessListener {
            dismiss()
        }.addOnFailureListener {
            showToast(requireContext(), it.message.toString())
        }
    }

    companion object {
        const val TAG = "profileBottomSheet"
    }
}