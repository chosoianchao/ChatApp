package com.rikkei.tranning.basekotlin

import androidx.recyclerview.widget.DiffUtil
import com.rikkei.tranning.basekotlin.model.Friends

class FriendsDiffCallBack(
    private var oldFriendsList: List<Friends>,
    private var newFriendsList: List<Friends>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldFriendsList.size
    }

    override fun getNewListSize(): Int {
        return newFriendsList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldFriendsList[oldItemPosition].Id == newFriendsList[newItemPosition].Id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldFriendsList[oldItemPosition] == newFriendsList[newItemPosition])
    }
}