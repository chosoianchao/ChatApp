package com.rikkei.tranning.basekotlin.fragment

import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FragmentMainChatBinding
import com.rikkei.tranning.basekotlin.viewmodel.MainChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainChatFragment : BaseFragment<FragmentMainChatBinding>() {
    override val layoutResource: Int
        get() = R.layout.fragment_main_chat

    override val viewModel: MainChatViewModel by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@MainChatFragment.viewModel
        }
    }

    override fun initViews() {

    }
}