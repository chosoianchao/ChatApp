package com.rikkei.tranning.basekotlin.fragment

import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.FriendsAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgMainChatBinding
import com.rikkei.tranning.basekotlin.model.Friends
import com.rikkei.tranning.basekotlin.viewmodel.MainChatVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainChatFrg : BaseFragment<FrgMainChatBinding>(), OnActionCallBack {
    override val layoutResource: Int
        get() = R.layout.frg_main_chat

    override val viewModel: MainChatVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@MainChatFrg.viewModel
        }
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        viewBinding.ivCreateNewMessage.setOnClickListener {
            val action = MainChatFrgDirections.actionMainChatFrgToCreateMessageWithFriendsFrg()
            findNavController().navigate(action)
        }
        showListChatFriends()
        viewModel.liveListFriends.observe(this, {
            val friendsAdapter: FriendsAdapter = viewBinding.rvListChat.adapter as FriendsAdapter
            friendsAdapter.updateFriendsListItems(it)
        })
    }

    private fun showListChatFriends() {
        viewBinding.rvListChat.layoutManager = LinearLayoutManager(context)
        viewBinding.rvListChat.hasFixedSize()
        viewBinding.rvListChat.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        if (viewModel.liveListFriends.value != null) {
            val friendsAdapter = FriendsAdapter(
                context, viewModel.liveListFriends.value!!,
                this
            )
            viewBinding.rvListChat.adapter = friendsAdapter
        }
        viewModel.getData()
    }

    override fun callBack(data: Any?, key: String) {
        val friends: Friends = data as Friends
        if (key == "ChatRoom") {
            val bundle = bundleOf(
                "Friends" to friends
            )
            findNavController().navigate(R.id.chatRoomFrg, bundle)
        }
    }
}