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
import com.rikkei.tranning.basekotlin.diff.UserDiff
import com.rikkei.tranning.basekotlin.model.User


class RequestAdapter(
    val context: Context?,
    var user: List<User>,
    var callBack: OnActionCallBack,
) :
    RecyclerView.Adapter<RequestAdapter.Companion.RequestViewHolder>() {

    fun updateUserListItems(userList: List<User>) {
        val oldList = user
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            UserDiff(oldList, userList)
        )
        user = userList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.item_all_friends, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val mUser: User = user[position]
        holder.name.text = mUser.Name
        if (mUser.PhotoUrl == "") {
            holder.avatar.setImageResource(R.drawable.ic_user)
        } else {
            context.let {
                if (it != null) {
                    Glide.with(it).load(mUser.PhotoUrl).into(holder.avatar)
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
            callBack.callBack(mUser, ADD_FRIENDS)
        }
    }


    override fun getItemCount(): Int {
        return user.size
    }

    companion object {
        class RequestViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.tv_name)
            val avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        }

        internal const val ADD_FRIENDS: String = "ADD_FRIENDS"
    }
}
