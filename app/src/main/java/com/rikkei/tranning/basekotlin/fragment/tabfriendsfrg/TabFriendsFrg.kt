package com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg

import androidx.fragment.app.viewModels
import com.rikkei.tranning.basekotlin.R
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
    }
}