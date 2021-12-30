package com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg

import androidx.fragment.app.viewModels
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgTabAllFrBinding
import com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm.TabAllVM

class TabAllFrg : BaseFragment<FrgTabAllFrBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_tab_all_fr

    override val viewModel: TabAllVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TabAllFrg.viewModel
        }
    }

    override fun initViews() {
    }
}