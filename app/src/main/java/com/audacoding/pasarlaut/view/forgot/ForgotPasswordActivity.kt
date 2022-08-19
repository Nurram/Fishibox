package com.audacoding.pasarlaut.view.forgot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audacoding.pasarlaut.databinding.ActivityForgotPasswordBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.visible
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ivBack.setOnClickListener { finish() }
            btnSubmit.setOnClickListener { sendResetPassword() }
        }
    }

    private fun sendResetPassword() {
        binding.loading.loadingScreen.visible()

        FirebaseAuth.getInstance().sendPasswordResetEmail(
            binding.etEmail.text.toString()
        ).addOnSuccessListener {
            binding.loading.loadingScreen.gone()
            showToast(this, "Link reset password telah dikirim ke email anda!")
            finish()
        }.addOnFailureListener {
            binding.loading.loadingScreen.gone()
            showToast(this, it.message.toString())
        }
    }
}