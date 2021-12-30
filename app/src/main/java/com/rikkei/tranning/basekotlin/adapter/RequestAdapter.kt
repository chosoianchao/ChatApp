package com.rikkei.tranning.basekotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.UserDiffCallBack
import com.rikkei.tranning.basekotlin.model.User
import de.hdodenhof.circleimageview.CircleImageView


class RequestAdapter(val context: Context?, var user: List<User>, val callBack: OnActionCallBack) :
    RecyclerView.Adapter<RequestAdapter.Companion.RequestViewHolder>() {

    fun updateUserListItems(userList: List<User>) {
        val oldList = user
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            UserDiffCallBack(oldList, userList)
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
        holder.addFriends.setOnClickListener {
            callBack.callBack(user[position], ADD_FRIENDS)
        }
    }

    override fun getItemCount(): Int {
        return user.size
    }

    companion object {
        class RequestViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val avatar: CircleImageView = itemView.findViewById(R.id.iv_avatar)
            val name: TextView = itemView.findViewById(R.id.tv_name)
            val addFriends: Button = itemView.findViewById(R.id.bt_add_friend)
        }

        const val ADD_FRIENDS: String = "ADD_FRIENDS"
    }
}
