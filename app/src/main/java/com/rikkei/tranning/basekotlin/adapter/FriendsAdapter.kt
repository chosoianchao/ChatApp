package com.rikkei.tranning.basekotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.diff.FriendsDiff
import com.rikkei.tranning.basekotlin.model.Friends


class FriendsAdapter(
    val context: Context?,
    var friends: List<Friends>,
    var callBack: OnActionCallBack,
) :
    RecyclerView.Adapter<FriendsAdapter.Companion.RequestViewHolder>() {

    fun updateFriendsListItems(friendsList: List<Friends>) {
        val oldList = friends
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            FriendsDiff(oldList, friendsList)
        )
        friends = friendsList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.item_all_friends, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val mFriends: Friends = friends[position]
        holder.name.text = mFriends.Name
        if (mFriends.PhotoUrl == "") {
            holder.avatar.setImageResource(R.drawable.ic_user)
        } else {
            context.let {
                if (it != null) {
                    Glide.with(it).load(mFriends.PhotoUrl).into(holder.avatar)
                }
            }
        }
        holder.itemView.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    androidx.appcompat.R.anim.abc_popup_enter
                )
            )
            callBack.callBack(mFriends, MESSAGE)
        }
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    companion object {
        class RequestViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.tv_name)
            val avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        }

        internal const val MESSAGE: String = "Message"
    }
}

