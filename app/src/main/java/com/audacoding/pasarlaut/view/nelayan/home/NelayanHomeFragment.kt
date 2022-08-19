package com.audacoding.pasarlaut.view.nelayan.home

import android.content.Intent
import android.os.Bundle
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
import com.audacoding.pasarlaut.view.nelayan.chat.NelayanChatActivity
import com.audacoding.pasarlaut.visible

class NelayanHomeFragment : Fragment() {
    private var _binding: FragmentNelayanHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NelayanHomeViewModel>()

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
        adapter = NelayanHomeAdapter(requireContext()) {
            val editBottomSheet = EditBottomSheet(it.id, it.productName, it.stock, it.price, it.desc)
            editBottomSheet.show(childFragmentManager, EditBottomSheet.TAG)
        }

        viewModel.getProducts()
        binding.apply {
            rvNelayanProduct.adapter = adapter
            rvNelayanProduct.layoutManager = LinearLayoutManager(requireContext())

            botNav.fabNelayanHome.setOnClickListener {
                val bottomSheet = AddBottomSheet()
                bottomSheet.show(childFragmentManager, AddBottomSheet.TAG)
            }
            botNav.bottomNavigationView.selectedItemId = R.id.nelayan_placeholder
            botNav.bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nelayan_profile -> {
                        botNav.bottomNavigationView.selectedItemId = R.id.nelayan_placeholder
                        findNavController().navigate(R.id.action_nelayanHomeFragment_to_profileFragment)
                        false
                    }
                    R.id.nelayan_chat -> {
                        botNav.bottomNavigationView.selectedItemId = R.id.nelayan_placeholder

                        val i = Intent(requireContext(), NelayanChatActivity::class.java)
                        startActivity(i)
                        false
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