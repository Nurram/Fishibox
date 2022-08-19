package com.audacoding.pasarlaut.view.user.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.*
import com.audacoding.pasarlaut.databinding.FragmentCartBinding
import com.audacoding.pasarlaut.view.user.detail.DetailActivity

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites()
        initListeners()

        adapter = CartAdapter(requireContext()) { product, pos ->
            if(pos >= 0) {
                viewModel.removeCart(pos)
            } else {
                val i = Intent(requireContext(), DetailActivity::class.java)
                i.putExtra("product", product)

                startActivity(i)
            }
        }

        binding.apply {
            rvFav.adapter = adapter
            rvFav.layoutManager = LinearLayoutManager(requireContext())
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
        viewModel.favorites.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}