package com.rikkei.tranning.basekotlin.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.MessageAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgChatRoomBinding
import com.rikkei.tranning.basekotlin.model.Friends
import com.rikkei.tranning.basekotlin.showToastLong
import com.rikkei.tranning.basekotlin.viewmodel.ChatRoomVM
import java.text.DateFormat
import java.util.*

class ChatRoomFrg : BaseFragment<FrgChatRoomBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_chat_room

    override val viewModel: ChatRoomVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatRoomFrg.viewModel
        }
    }

    override fun initViews() {
        val bundle = arguments
        val friends = bundle?.getParcelable<Friends>("Friends")!!
        if (friends.PhotoUrl == "") {
            viewBinding.tvName.text = friends.Name
            viewBinding.ivAvatar.setImageResource(R.drawable.ic_user)
        } else {
            viewBinding.tvName.text = friends.Name
            context?.let { Glide.with(it).load(friends.PhotoUrl).into(viewBinding.ivAvatar) }
        }
        viewBinding.ivBack.setOnClickListener {
            val action = ChatRoomFrgDirections.actionChatRoomFrgToMainChatFrg()
            findNavController().navigate(action)
        }

        viewModel.liveListChats.observe(this, {
            val chatAdapter: MessageAdapter = viewBinding.rvChatRoom.adapter as MessageAdapter
            chatAdapter.updateChatListItems(it)
        })
        showListChat(friends)
        viewBinding.ivSend.setOnClickListener {
            val msg = viewBinding.edtMessage.text.toString()
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val minute = Calendar.getInstance().get(Calendar.MINUTE)
            val time = "$hour:$minute"
            val date = DateFormat.getDateInstance().format(Date())

            if (msg != "") {
                viewModel.senderID?.let { senderID ->
                    viewModel.sendMessage(
                        senderID,
                        friends.Id,
                        msg, time, date
                    )
                }
            } else {
                context?.showToastLong(getString(R.string.empty_message))
            }
            viewBinding.edtMessage.setText("")
        }
    }

    private fun showListChat(friends: Friends) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        viewBinding.rvChatRoom.layoutManager = linearLayoutManager
        viewBinding.rvChatRoom.hasFixedSize()

        if (viewModel.liveListChats.value != null) {
            val chatAdapter = MessageAdapter(
                viewModel.senderID, context, friends.PhotoUrl, viewModel.liveListChats.value!!
            )
            viewBinding.rvChatRoom.adapter = chatAdapter
        }
        viewModel.senderID?.let {
            viewModel.getData(
                it,
                friends.Id,
                friends.PhotoUrl,
                ::actionDate,
                ::actionToday

            )
        }
    }

    private fun actionDate(s: String) {
        viewBinding.tvDate.visibility = View.VISIBLE
        viewBinding.tvDate.text = s
    }

    private fun actionToday() {
        viewBinding.tvDate.visibility = View.VISIBLE
        viewBinding.tvDate.text = getString(R.string.text_today)
    }
}