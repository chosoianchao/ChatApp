package com.rikkei.tranning.basekotlin.fragment

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.FriendsAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgCreateMessageBinding
import com.rikkei.tranning.basekotlin.model.Friends
import com.rikkei.tranning.basekotlin.viewmodel.CreateMessageWithFriendsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateMessageWithFriendsFrg : BaseFragment<FrgCreateMessageBinding>(), OnActionCallBack {
    override val layoutResource: Int
        get() = R.layout.frg_create_message
    override val viewModel: CreateMessageWithFriendsVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@CreateMessageWithFriendsFrg.viewModel
        }
    }

    override fun initViews() {
        showListRequest()
        viewModel.liveListFriends.observe(this, {
            val friendsAdapter: FriendsAdapter = viewBinding.rvListMessage.adapter as FriendsAdapter
            friendsAdapter.updateFriendsListItems(it)
        })
    }

    private fun showListRequest() {
        viewBinding.rvListMessage.layoutManager = LinearLayoutManager(context)
        viewBinding.rvListMessage.hasFixedSize()
        viewBinding.rvListMessage.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        if (viewModel.liveListFriends.value != null) {
            val friendsAdapter = FriendsAdapter(
                context, viewModel.liveListFriends.value!!,
                this
            )
            viewBinding.rvListMessage.adapter = friendsAdapter
        }
        viewModel.getData()
    }

    override fun callBack(data: Any?, key: String) {
        if (key == FriendsAdapter.MESSAGE) {
            val friends: Friends = data as Friends
            if (friends.PhotoUrl == "") {
                viewBinding.ivUser.setImageResource(R.drawable.ic_user)
                viewBinding.ivUser.visibility = View.VISIBLE
                viewBinding.ivDelete.visibility = View.VISIBLE
            } else {
                context?.let {
                    Glide.with(it).load(friends.PhotoUrl).into(viewBinding.ivUser)
                    viewBinding.ivUser.visibility = View.VISIBLE
                    viewBinding.ivForward.isEnabled = true
                    viewBinding.ivDelete.visibility = View.VISIBLE
                }
            }
            viewBinding.ivDelete.setOnClickListener {
                viewBinding.ivUser.setImageResource(android.R.color.transparent)
                viewBinding.ivDelete.visibility = View.INVISIBLE
                viewBinding.ivForward.isEnabled = false
            }
            viewBinding.ivForward.setOnClickListener {
                val bundle = bundleOf(
                    "Friends" to friends
                )
                findNavController().navigate(R.id.chatRoomFrg, bundle)
            }
        }
    }
}