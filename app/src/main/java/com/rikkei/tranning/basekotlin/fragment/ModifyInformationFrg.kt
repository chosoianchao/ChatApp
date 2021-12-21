package com.rikkei.tranning.basekotlin.fragment

import androidx.fragment.app.viewModels
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgModifyInformationBinding
import com.rikkei.tranning.basekotlin.viewmodel.ModifyInformationVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyInformationFrg : BaseFragment<FrgModifyInformationBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_modify_information
    override val viewModel: ModifyInformationVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ModifyInformationFrg.viewModel
        }
    }

    override fun initViews() {
    }
}