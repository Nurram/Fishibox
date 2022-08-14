package com.audacoding.pasarlaut.view.nelayan.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.audacoding.pasarlaut.R
import com.audacoding.pasarlaut.databinding.FragmentNelayanHomeBinding
import com.audacoding.pasarlaut.gone
import com.audacoding.pasarlaut.showToast
import com.audacoding.pasarlaut.visible

class NelayanHomeFragment : Fragment() {
    private var _binding: FragmentNelayanHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModels<NelayanHomeViewModel>()

    private lateinit var adapter: NelayanHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNelayanHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botNav = binding.vBotnav
        val bottomSheet = AddBottomSheet()
        adapter = NelayanHomeAdapter(requireContext()) {
            val editBottomSheet = EditBottomSheet(it.id, it.productName, it.productStock, it.desc)
            editBottomSheet.show(childFragmentManager, EditBottomSheet.TAG)
        }

        viewModel.getProducts()
        binding.apply {
            rvNelayanProduct.adapter = adapter
            rvNelayanProduct.layoutManager = LinearLayoutManager(requireContext())

            botNav.fabNelayanHome.setOnClickListener {
                bottomSheet.show(childFragmentManager, AddBottomSheet.TAG)
            }
            botNav.bottomNavigationView.selectedItemId = R.id.nelayan_placeholder
            botNav.bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nelayan_profile -> {
                        findNavController().navigate(R.id.action_nelayanHomeFragment_to_profileFragment)
                        botNav.bottomNavigationView.selectedItemId = R.id.nelayan_placeholder
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }

        initListeners()
    }

    private fun initListeners() {
        viewModel.onLoading.observe(viewLifecycleOwner) {
            if(it) {
                binding.loading.loadingScreen.visible()
            } else {
                binding.loading.loadingScreen.gone()
            }
        }
        viewModel.onError.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                showToast(requireContext(), it)
            }
        }
        viewModel.products.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                adapter.submitList(it)
            }
        }
    }
}