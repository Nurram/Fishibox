package com.audacoding.pasarlaut.view.user.checkout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.ActivityCheckoutBinding
import com.audacoding.pasarlaut.models.NelayanProduct
import com.bumptech.glide.Glide
import com.google.type.DateTime
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    private val viewModel by viewModels<CheckoutViewModel>()

    private var paymentMethod = "gojek"
    private var sendingMethod = "cod"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val product = intent.getParcelableExtra<NelayanProduct>("product")

        initListeners()

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvName.text = product?.productName
            tvPrice.text = "Rp${product?.price}"

            Glide.with(this@CheckoutActivity).load(product?.imgUrl).into(ivImg)

            ivBack.setOnClickListener { finish() }
            ivAdd.setOnClickListener {
                var currentText = etQty.text.toString().toInt()

                if(currentText < 999) {
                    currentText += 1
                    etQty.setText(currentText.toString())

                    val priceInt = product?.price?.toInt()
                    if(priceInt != null) {
                        tvPrice.text = "Rp${priceInt * currentText}"
                    }
                }
            }
            ivMin.setOnClickListener {
                var currentText = etQty.text.toString().toInt()

                if(currentText > 1) {
                    currentText -= 1
                    etQty.setText(currentText.toString())

                    val priceInt = product?.price?.toInt()
                    if(priceInt != null) {
                        tvPrice.text = "Rp${priceInt * currentText}"
                    }
                }
            }
            tvSend.setOnClickListener {
                val sendBottomSheet = SendBottomSheet()
                sendBottomSheet.show(supportFragmentManager, SendBottomSheet.TAG)
            }
            tvPayment.setOnClickListener {
                val paymentBottomSheet = PaymentBottomSheet()
                paymentBottomSheet.show(supportFragmentManager, PaymentBottomSheet.TAG)
            }
            btnBuy.setOnClickListener {
                if(etAddress.text.isNotEmpty()) {
                    if(paymentMethod.isNotEmpty() || sendingMethod.isNotEmpty()) {
                        viewModel.saveOrder(
                            mutableMapOf(
                                "uid" to "",
                                "productId" to product!!.id.toString(),
                                "productName" to product.productName,
                                "imgUrl" to product.imgUrl,
                                "price" to (product.price.toInt() * etQty.text.toString().toInt()),
                                "address" to etAddress.text.toString(),
                                "qty" to etQty.text.toString(),
                                "paymentMethod" to paymentMethod,
                                "sendingMethod" to sendingMethod,
                                "orderDate" to Calendar.getInstance().timeInMillis
                            )
                        )
                    } else {
                        showToast(this@CheckoutActivity, "Metode pembayaran / pengiriman belum dipilih!")
                    }
                } else {
                    showToast(this@CheckoutActivity, "Alamat belum diisi!")
                }
            }
        }
    }

    private fun initListeners() {
        viewModel.onLoading.observe(this) {
            if(it) {
                binding.loading.loadingScreen.visible()
                setScreenUntouchable(this)
            } else {
                binding.loading.loadingScreen.gone()
                setScreenTouchable(this)
            }
        }
        viewModel.onError.observe(this) {
            if(it.isNotEmpty()) {
                showToast(this, it)
                finish()
            }
        }
        viewModel.onSuccess.observe(this) {
            if(it) {
                showToast(this, "Order anda telah berhasil!")
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setPaymentMethod(method: String) {
        paymentMethod = method
        binding.tvPayment.text = "Pengiriman: (${method.uppercase()})"
    }

    @SuppressLint("SetTextI18n")
    fun setSendingMethod(method: String) {
        sendingMethod = method
        binding.tvSend.text = "Metode Pembayaran: (${method.uppercase()})"
    }
}