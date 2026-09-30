package com.aistudyos.app.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aistudyos.app.databinding.ItemChatMessageBinding
import com.aistudyos.app.domain.model.ChatMessage

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.VH>(DiffCallback) {

    inner class VH(val b: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val msg = getItem(position)

        // 🔁 Reset views
        holder.b.layoutUser.visibility = View.GONE
        holder.b.layoutAssistant.visibility = View.GONE
        holder.b.tvSources.visibility = View.GONE

        if (msg.role == "user") {
            holder.b.layoutUser.visibility = View.VISIBLE
            holder.b.tvMessageUser.text = msg.content
        } else {
            holder.b.layoutAssistant.visibility = View.VISIBLE
            holder.b.tvMessageAssistant.text = msg.content
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(a: ChatMessage, b: ChatMessage) = a.id == b.id
        override fun areContentsTheSame(a: ChatMessage, b: ChatMessage) = a == b
    }
}