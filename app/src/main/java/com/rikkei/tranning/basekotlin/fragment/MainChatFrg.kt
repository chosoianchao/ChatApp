package com.rikkei.tranning.basekotlin.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgMainChatBinding
import com.rikkei.tranning.basekotlin.viewmodel.MainChatVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainChatFrg : BaseFragment<FrgMainChatBinding>() {
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
    }
}