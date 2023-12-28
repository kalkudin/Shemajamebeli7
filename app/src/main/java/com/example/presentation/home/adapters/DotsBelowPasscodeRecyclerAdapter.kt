package com.example.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.home.model.DotBelowThePasscode
import com.example.shemajamebeli7.databinding.ProgressSmallCircleLayoutBinding

class DotsBelowPasscodeRecyclerAdapter :
    ListAdapter<DotBelowThePasscode, DotsBelowPasscodeRecyclerAdapter.CircleViewHolder>(
        CircleDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleViewHolder {
        val binding = ProgressSmallCircleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CircleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CircleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CircleViewHolder(private val binding: ProgressSmallCircleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DotBelowThePasscode) {
            binding.icon.setImageResource(item.imagePath)
        }
    }

    private class CircleDiffCallback : DiffUtil.ItemCallback<DotBelowThePasscode>() {
        override fun areItemsTheSame(
            oldItem: DotBelowThePasscode,
            newItem: DotBelowThePasscode
        ): Boolean {
            return oldItem.imagePath == newItem.imagePath
        }

        override fun areContentsTheSame(
            oldItem: DotBelowThePasscode,
            newItem: DotBelowThePasscode
        ): Boolean {
            return oldItem == newItem
        }
    }
}
