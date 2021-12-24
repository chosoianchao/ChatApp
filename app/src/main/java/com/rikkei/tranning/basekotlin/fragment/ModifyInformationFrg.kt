package com.rikkei.tranning.basekotlin.fragment

import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        viewModel.getInformation(::getInformation)
        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.tvSave.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    androidx.appcompat.R.anim.abc_popup_enter
                )
            )
            val name = viewBinding.edtFullName.text.toString()
            val phone = viewBinding.edtPhone.text.toString()
            val date = viewBinding.edtDate.text.toString()
            viewModel.updateProfile(name, phone, date)
        }
    }

    private fun getInformation(name: Any?, phone: Any?, date: Any?) {
        viewBinding.edtFullName.setText(name.toString())
        viewBinding.edtPhone.setText(phone.toString())
        viewBinding.edtDate.setText(date.toString())
    }
}