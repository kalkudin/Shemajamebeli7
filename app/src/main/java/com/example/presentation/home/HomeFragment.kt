package com.example.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.common.BaseFragment
import com.example.shemajamebeli7.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels()
    private val keyAdapter = HomeKeyRecyclerAdapter()

    override fun bind() {
        binding.keyRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = keyAdapter
        }
    }

    override fun bindViewActionListeners() {
        // Set up any click listeners or user interactions here if needed
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.recyclerItemList.collect(){
                    keyAdapter.submitList(it)
                }
            }
        }
    }
}