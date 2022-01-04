package com.rikkei.tranning.basekotlin

import androidx.recyclerview.widget.DiffUtil
import com.rikkei.tranning.basekotlin.model.ChatMessage

class MessageDiffCallBack(
    private var oldChatList: List<ChatMessage>,
    private var newChatList: List<ChatMessage>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldChatList.size
    }

    override fun getNewListSize(): Int {
        return newChatList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldChatList[oldItemPosition].Message == newChatList[newItemPosition].Message)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldChatList[oldItemPosition] == newChatList[newItemPosition])
    }
}