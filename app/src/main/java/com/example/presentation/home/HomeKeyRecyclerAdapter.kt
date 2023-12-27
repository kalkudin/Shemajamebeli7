package com.example.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.home.model.Key
import com.example.shemajamebeli7.R
import com.example.shemajamebeli7.databinding.KeyLayoutBinding

class HomeKeyRecyclerAdapter :
    ListAdapter<Key, HomeKeyRecyclerAdapter.KeyViewHolder>(KeyDiffCallback()) {

    class KeyViewHolder(private val binding: KeyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Key) {
            binding.number.text = item.number?.toString() ?: ""
            binding.icon.setImageResource(item.icon ?: R.drawable.ic_default_icon)
            binding.icon.visibility = if (item.itemType.ordinal == Key.ItemType.IMAGE.ordinal) View.VISIBLE else View.GONE
            binding.number.visibility = if (item.itemType.ordinal == Key.ItemType.NUMBER.ordinal) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyViewHolder {
        val binding = KeyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class KeyDiffCallback : DiffUtil.ItemCallback<Key>() {
        override fun areItemsTheSame(oldItem: Key, newItem: Key): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Key, newItem: Key): Boolean {
            return oldItem == newItem
        }
    }
}