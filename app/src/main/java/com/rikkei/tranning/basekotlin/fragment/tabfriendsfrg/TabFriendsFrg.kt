package com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.FriendsAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgTabFriendsBinding
import com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm.TabFriendsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabFriendsFrg : BaseFragment<FrgTabFriendsBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_tab_friends

    override val viewModel: TabFriendsVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TabFriendsFrg.viewModel
        }
    }

    override fun initViews() {
        showListRequest()
        viewModel.liveListFriends.observe(this, {
            val friendsAdapter: FriendsAdapter = viewBinding.rvListFriend.adapter as FriendsAdapter
            friendsAdapter.updateFriendsListItems(it)
        })
    }

    private fun showListRequest() {
        viewBinding.rvListFriend.layoutManager = LinearLayoutManager(context)
        viewBinding.rvListFriend.hasFixedSize()
        viewBinding.rvListFriend.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        if (viewModel.liveListFriends.value != null) {
            val friendsAdapter = FriendsAdapter(
                context, viewModel.liveListFriends.value!!,
                object : OnActionCallBack {
                    override fun callBack(data: Any?, key: String) {

                    }
                }
            )
            viewBinding.rvListFriend.adapter = friendsAdapter
        }
        viewModel.getData()
    }
}