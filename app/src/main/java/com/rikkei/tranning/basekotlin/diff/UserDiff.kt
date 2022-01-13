package com.rikkei.tranning.basekotlin.diff

import androidx.recyclerview.widget.DiffUtil
import com.rikkei.tranning.basekotlin.model.User

class UserDiff(private var oldUserList: List<User>, private var newUserList: List<User>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldUserList.size
    }

    override fun getNewListSize(): Int {
        return newUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldUserList[oldItemPosition].Id == newUserList[newItemPosition].Id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldUserList[oldItemPosition] == newUserList[newItemPosition])
    }
}