package com.audacoding.pasarlaut.view.user.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.FragmentDashboardBinding
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.user.detail.DetailActivity

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<DashboardViewModel>()
    private val products = arrayListOf<NelayanProduct>()

    private lateinit var adapter: DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts()
        initListeners()

        adapter = DashboardAdapter(requireContext()) {
            val i = Intent(requireContext(), DetailActivity::class.java)
            i.putExtra("product", it)

            startActivity(i)
        }

        binding.apply {
            rvProduct.adapter = adapter
            rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
            etSearch.addTextChangedListener {
                if(it != null && it.isNotEmpty()) {
                    val productsCopy = products.toMutableList()
                    val filteredData = productsCopy.filter { product ->
                        val name = product.productName.lowercase()
                        name.contains(it.toString().lowercase())
                    }

                    adapter.submitList(filteredData)
                } else {
                    adapter.submitList(products)
                }
            }
        }
    }

    private fun initListeners() {
        viewModel.onLoading.observe(viewLifecycleOwner) {
            if(it) {
                binding.loading.loadingScreen.visible()
                setScreenUntouchable(requireActivity())
            } else {
                binding.loading.loadingScreen.gone()
                setScreenTouchable(requireActivity())
            }
        }
        viewModel.onError.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                showToast(requireContext(), it)
            }
        }
        viewModel.products.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                products.clear()
                products.addAll(it)
                adapter.submitList(it)
            }
        }
    }
}