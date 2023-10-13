package com.example.chat_app.chatactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.databinding.RecievedMessageItemBinding
import com.example.chat_app.databinding.SentMessageItemBinding
import com.example.chat_app.firestore.model.ChatMessage

enum class ViewType(val value: Int) {
    sent(1),
    received(2)
}

class MessagesAdapter(var list: MutableList<ChatMessage>) : Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            ViewType.sent.value -> {
                val itemBinding =
                    SentMessageItemBinding
                        .inflate(
                            LayoutInflater
                                .from(parent.context), parent, false
                        )
                return SendViewHolder(itemBinding)
            }

            else -> {
                val itemBinding =
                    RecievedMessageItemBinding
                        .inflate(
                            LayoutInflater
                                .from(parent.context), parent, false
                        )
                return ReceivedViewHolder(itemBinding)
            }
        }
    }

    fun bindMessages(it: MutableList<ChatMessage>) {
        var oldPosition = list.size
        this.list.addAll(it)
        notifyItemRangeInserted(oldPosition, list.size)
    }

    override fun getItemCount(): Int = list.size


    override fun getItemViewType(position: Int): Int {
        return if (list[position].senderId == SessionProvider.user?.id)
            ViewType.sent.value
        else
            ViewType.received.value
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SendViewHolder -> holder.bind(list[position])
            is ReceivedViewHolder -> holder.bind(list[position])
        }
    }
}

class SendViewHolder(
    var itemBinding: SentMessageItemBinding
) :
    ViewHolder(itemBinding.root) {
    fun bind(item: ChatMessage) {
        itemBinding.chatMessage = item
        itemBinding.user = SessionProvider.user
        itemBinding.executePendingBindings()

    }
}

class ReceivedViewHolder(
    var itemBinding: RecievedMessageItemBinding
) :
    ViewHolder(itemBinding.root) {
    fun bind(item: ChatMessage) {
        itemBinding.chatMessage = item
        itemBinding.executePendingBindings()
    }
}