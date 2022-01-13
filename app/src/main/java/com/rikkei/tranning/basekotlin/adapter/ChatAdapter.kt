package com.rikkei.tranning.basekotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.model.Message
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale


class ChatAdapter(
    val context: Context?,
    private var uId: String?,
    var imageUrl: String?,
    private var chat: List<Message>
) :
    RecyclerView.Adapter<ChatAdapter.Companion.MessageHolder>() {

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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(chatList: List<Message>) {
        chat = chatList
        notifyDataSetChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val yesterday: String = dateFormat.format(calendar.time)
        val mChat: Message = chat[position]
        val type = mChat.type
        if (type == "text") {
            holder.showMessage.text = mChat.message
            holder.imageMessage.visibility = View.GONE
        } else {
            holder.showMessage.visibility = View.GONE
            context?.let { Glide.with(it).load(mChat.message).into(holder.imageMessage) }
        }

        if (imageUrl == "") {
            holder.avatar.setImageResource(R.drawable.ic_user)
        } else {
            context?.let { Glide.with(it).load(imageUrl).into(holder.avatar) }
        }
        holder.showMessage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    holder.date.visibility = View.VISIBLE
                    when (mChat.time) {
                        currentDate -> {
                            holder.date.text = context?.getString(R.string.text_today)
                        }
                        yesterday -> {
                            holder.date.text = context?.getString(R.string.text_yesterday)
                        }
                        else -> {
                            holder.date.text = mChat.time
                        }
                    }
                    return true
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    holder.date.visibility = View.GONE
                    return true
                }
                return false
            }
        })
        holder.imageMessage.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("SetTextI18n")
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    holder.imageTime.visibility = View.VISIBLE
                    when (mChat.time) {
                        currentDate -> {
                            holder.imageTime.text =
                                context?.getString(R.string.text_today) + " " + mChat.seen
                        }
                        yesterday -> {
                            holder.imageTime.text =
                                context?.getString(R.string.text_yesterday) + " " + mChat.seen
                        }
                        else -> {
                            holder.imageTime.text = mChat.time + " " + mChat.seen
                        }
                    }
                    return true
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    holder.imageTime.visibility = View.GONE
                    return true
                }
                return false
            }
        })
        holder.time.text = mChat.seen
    }

    override fun getItemViewType(position: Int): Int {
        val mChat: Message = chat[position]
        val fromUser = mChat.from

        return if (fromUser.equals(uId)) {
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
            val imageMessage: ImageView = itemView.findViewById(R.id.iv_show_message)
            val date: TextView = itemView.findViewById(R.id.tv_show_date)
            val time: TextView = itemView.findViewById(R.id.tv_time)
            val imageTime: TextView = itemView.findViewById(R.id.tv_image_time)
        }

        private const val MSG_TYPE_LEFT: Int = 0
        private const val MSG_TYPE_RIGHT: Int = 1
    }
}

