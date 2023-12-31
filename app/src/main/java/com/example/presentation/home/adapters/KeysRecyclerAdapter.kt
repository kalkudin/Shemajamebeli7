package com.example.presentation.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.home.model.Key
import com.example.shemajamebeli7.R
import com.example.shemajamebeli7.databinding.KeyLayoutBinding

class KeysRecyclerAdapter(private val itemClickListener: (Key) -> Unit) :
    ListAdapter<Key, KeysRecyclerAdapter.KeyViewHolder>(KeyDiffCallback()) {

    inner class KeyViewHolder(private val binding: KeyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Key) {
            with(binding){
                number.text = item.number?.toString() ?: ""
                icon.setImageResource(item.icon ?: R.drawable.ic_default_icon)
                icon.visibility = if (item.itemType == Key.ItemType.IMAGE) View.VISIBLE else View.GONE
                number.visibility = if (item.itemType == Key.ItemType.NUMBER) View.VISIBLE else View.GONE

                root.setOnClickListener {
                    val position= adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val key = getItem(position)
                        itemClickListener.invoke(key)
                    }
                }
            }
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