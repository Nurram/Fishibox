package com.audacoding.pasarlaut.view.user.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import com.audacoding.pasarlaut.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SendBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rbGojek = view.findViewById<RadioButton>(R.id.rb_gojek)
        val rbGrab = view.findViewById<RadioButton>(R.id.rb_grab)

        val btnSave = view.findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {
            val method = if(rbGojek.isChecked) {
                "gojek"
            } else if(rbGrab.isChecked) {
                "grab"
            } else {
                ""
            }

            (activity as CheckoutActivity).setSendingMethod(method)
            dismiss()
        }
    }

    companion object {
        const val TAG = "sendBottomSheet"
    }
}