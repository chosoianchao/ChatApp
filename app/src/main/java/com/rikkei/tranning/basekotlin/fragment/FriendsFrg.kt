package com.rikkei.tranning.basekotlin.fragment

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.TabAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgFriendsBinding
import com.rikkei.tranning.basekotlin.viewmodel.FriendsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFrg : BaseFragment<FrgFriendsBinding>() {
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
        val tabLayout = viewBinding.tlFriend
        val viewPager = viewBinding.viewPager

        val tabAdapter = TabAdapter(this)
        viewPager.adapter = tabAdapter

        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Friends"
                1 -> tab.text = "ALL"
            }
        }.attach()
    }
}