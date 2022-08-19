package com.audacoding.pasarlaut.view.user.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.view.user.detail.DetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_botom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<Button>(R.id.btn_save)
        val rbCod = view.findViewById<RadioButton>(R.id.rb_cod)
        val rbOvo = view.findViewById<RadioButton>(R.id.rb_ovo)
        val rbGopay = view.findViewById<RadioButton>(R.id.rb_gopay)

        btnSave.setOnClickListener {
            val paymentMethod = if(rbCod.isChecked) {
                "cod"
            } else if(rbOvo.isChecked) {
                "ovo"
            } else if(rbGopay.isChecked){
                "gopay"
            } else {
                ""
            }

            (activity as CheckoutActivity).setPaymentMethod(paymentMethod)
            dismiss()
        }
    }

    companion object {
        const val TAG = "paymentBottomSheet"
    }
}