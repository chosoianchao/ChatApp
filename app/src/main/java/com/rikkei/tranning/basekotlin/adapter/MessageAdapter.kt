package com.rikkei.tranning.basekotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.MessageDiffCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.model.ChatMessage
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(
    private val currentId: String?,
    val context: Context?,
    private val imageUrl: String?,
    private var chat: List<ChatMessage>,
) :
    RecyclerView.Adapter<MessageAdapter.Companion.MessageHolder>() {

    fun updateChatListItems(chatList: List<ChatMessage>) {
        val oldList = chat
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            MessageDiffCallBack(oldList, chatList)
        )
        chat = chatList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        return if (viewType == MSG_TYPE_RIGHT) {
            val view: View = layoutInflater.inflate(R.layout.item_chat_right, parent, false)
            MessageHolder(view)
        } else {
            val view: View = layoutInflater.inflate(R.layout.item_chat_left, parent, false)
            MessageHolder(view)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val mChat: ChatMessage = chat[position]
        holder.showMessage.text = mChat.Message
        holder.time.text = mChat.Time
        holder.showMessage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    holder.date.visibility = View.VISIBLE
                    holder.date.text = mChat.Date
                    return true
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    holder.date.visibility = View.GONE
                    return true
                }
                return false
            }
        })

        if (imageUrl == "") {
            holder.avatar.setImageResource(R.drawable.ic_user)
        } else {
            context?.let { Glide.with(it).load(imageUrl).into(holder.avatar) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentId == chat[position].SenderID) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    companion object {
        class MessageHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val showMessage: TextView = itemView.findViewById(R.id.tv_show_message)
            val avatar: CircleImageView = itemView.findViewById(R.id.iv_avatar)
            val time: TextView = itemView.findViewById(R.id.tv_time)
            val date: TextView = itemView.findViewById(R.id.tv_show_date)
        }

        private const val MSG_TYPE_LEFT: Int = 0
        private const val MSG_TYPE_RIGHT: Int = 1
    }
}

