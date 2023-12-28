package com.example.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.home.model.Dot
import com.example.shemajamebeli7.databinding.DotLayoutBinding

class DotsRecyclerAdapter : ListAdapter<Dot, DotsRecyclerAdapter.DotViewHolder>(DotDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DotViewHolder {
        val binding = DotLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DotViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DotViewHolder(private val binding: DotLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Dot) {
            binding.icon.setImageResource(item.imagePath)
        }
    }

    private class DotDiffCallback : DiffUtil.ItemCallback<Dot>() {
        override fun areItemsTheSame(oldItem: Dot, newItem: Dot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dot, newItem: Dot): Boolean {
            return oldItem == newItem
        }
    }
}
