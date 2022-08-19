package com.audacoding.pasarlaut.view.user.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HistoryViewModel>()

    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHistories()
        initListeners()

        adapter = HistoryAdapter(this) {
            val sheet = HistoryDetailBottomSheet(it)
            sheet.show(supportFragmentManager, HistoryDetailBottomSheet.TAG)
        }
        binding.apply {
            ivBack.setOnClickListener { finish() }
            rvHistory.adapter = adapter
            rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
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
        viewModel.orders.observe(this) {
            adapter.submitList(it)
        }
    }
}