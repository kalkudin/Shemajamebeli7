package com.example.presentation.home

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.BaseFragment
import com.example.data.common.Resource
import com.example.presentation.home.adapters.DotsRecyclerAdapter
import com.example.presentation.home.adapters.KeysRecyclerAdapter
import com.example.presentation.home.events.HomeKeyClickEvent
import com.example.presentation.home.model.Key
import com.example.shemajamebeli7.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels()

    private val dotsAdapter = DotsRecyclerAdapter()

    private val keyAdapter = KeysRecyclerAdapter { item ->
        handleItemClick(item)
    }

    override fun bind() {
        bindKeyRecyclerView()
        bindCirclesBelowPasscodeRecyclerView()
    }

    override fun bindViewActionListeners() {
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.recyclerItemList.collect(){
                    keyAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.successFlow.collect(){resource ->
                    when(resource){
                        is Resource.Success -> Toast.makeText(requireContext(), resource.successMessage, Toast.LENGTH_SHORT).show()
                        is Resource.Failure -> Toast.makeText(requireContext(), resource.failureMessage, Toast.LENGTH_SHORT).show()
                        is Resource.Nothing -> doNothing()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.filledKeysFlow.collect{ it ->
                    dotsAdapter.submitList(it)
                }
            }
        }
    }

    private fun handleItemClick(item: Key) {
        when (item.itemFunctionality) {
            Key.ItemFunction.SCAN -> {
                homeViewModel.onEvent(event = HomeKeyClickEvent.ScanUserFingerPrint)
            }
            Key.ItemFunction.NONE -> {
                homeViewModel.onEvent(event = HomeKeyClickEvent.AddKeyToPassword(number = item.number!!))
            }
            Key.ItemFunction.DELETE -> {
                homeViewModel.onEvent(event = HomeKeyClickEvent.DeletePasswordKey)
            }
        }
    }

    private fun bindKeyRecyclerView() {
        binding.keyRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = keyAdapter
        }
    }

    private fun bindCirclesBelowPasscodeRecyclerView() {
        binding.circlesBelowPasscodeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = dotsAdapter
        }
    }

    private fun doNothing() {

    }
}