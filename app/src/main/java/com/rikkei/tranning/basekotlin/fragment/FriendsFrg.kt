package com.rikkei.tranning.basekotlin.fragment

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.FriendsAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgFriendsBinding
import com.rikkei.tranning.basekotlin.viewmodel.FriendsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFrg : BaseFragment<FrgFriendsBinding>(), OnActionCallBack {
    var friendsAdapter: FriendsAdapter? = null
    override val layoutResource: Int
        get() = R.layout.frg_friends

    override val viewModel: FriendsVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@FriendsFrg.viewModel
        }
    }

    override fun initViews() {
        viewBinding.ivAddNewFriends.setOnClickListener {
            val action = FriendsFrgDirections.actionFriendsFrgToTabAllFrg()
            findNavController().navigate(action)
        }
        viewBinding.rvListFriend.layoutManager = LinearLayoutManager(context)
        viewBinding.rvListFriend.hasFixedSize()
        viewBinding.rvListFriend.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        showListFriends()
        viewModel.liveListFriends.observe(this, {
            friendsAdapter = viewBinding.rvListFriend.adapter as FriendsAdapter
            friendsAdapter!!.updateFriendsListItems(it)
        })
        showListSearch()
        viewModel.liveListSearch.observe(this, {
            friendsAdapter = viewBinding.rvListFriend.adapter as FriendsAdapter
            friendsAdapter!!.updateFriendsListItems(it)
        })
    }

    private fun showListSearch() {
        if (viewModel.liveListFriends.value != null) {
            friendsAdapter = FriendsAdapter(context, viewModel.liveListSearch.value!!, this)
            viewBinding.rvListFriend.adapter = friendsAdapter
        }
        viewBinding.edtSearchFriends.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchFriends(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun showListFriends() {
        if (viewModel.liveListFriends.value != null) {
            friendsAdapter = FriendsAdapter(context, viewModel.liveListFriends.value!!, this)
            viewBinding.rvListFriend.adapter = friendsAdapter
        }
        viewModel.getData(viewBinding.edtSearchFriends.text.toString())
    }

    override fun callBack(data: Any?, key: String) {
    }
}
