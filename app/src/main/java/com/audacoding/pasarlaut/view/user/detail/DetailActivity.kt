package com.audacoding.pasarlaut.view.user.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.ActivityDetailBinding
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.user.chatDetail.ChatDetailActivity
import com.audacoding.pasarlaut.view.user.checkout.CheckoutActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var product: NelayanProduct

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel>()
    private var isFavorite = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product = intent?.getParcelableExtra("product")!!

        initListeners()
        viewModel.checkIsFav(product.id)

        binding.apply {
            tvName.text = product.productName
            tvPrice.text = "Rp${product.price}"
            tvDesc.text = product.desc

            ivBack.setOnClickListener { finish() }
            ivFav.setOnClickListener {
                if(!isFavorite) {
                    viewModel.addToFav(product)
                } else {
                    viewModel.removeFromFav()
                }
            }
            ivCart.setOnClickListener {
                viewModel.addToCart(product)
            }
            ivMsg.setOnClickListener {
                val i = Intent(this@DetailActivity, ChatDetailActivity::class.java)
                i.putExtra("product", product)

                startActivity(i)
            }
            btnCheckout.setOnClickListener {
                val i = Intent(this@DetailActivity, CheckoutActivity::class.java)
                i.putExtra("product", product)

                startActivity(i)
            }

            Glide.with(this@DetailActivity).load(product.imgUrl).into(ivImg)
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
        viewModel.productAdded.observe(this) {
            if(it != null) {
                if(it["value"] == true) {
                    showToast(this, "Data Telah Disimpan di ${it["desc"]}")

                    if(it["desc"] == "Cart") {
                        val i = Intent(this, CheckoutActivity::class.java)
                        i.putExtra("product", product)

                        startActivity(i)
                    }
                }
            }
        }
        viewModel.productFavorite.observe(this) {
            isFavorite = it

            if (it) {
                binding.ivFav.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                binding.ivFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
        viewModel.onDelete.observe(this) {
            if(it) {
                showToast(this, getString(R.string.data_deleted))
            }
        }
    }
}