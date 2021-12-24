package com.rikkei.tranning.basekotlin.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgForgetPasswordBinding
import com.rikkei.tranning.basekotlin.showToastLong
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.ForgotVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotFrg : BaseFragment<FrgForgetPasswordBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_forget_password

    override val viewModel: ForgotVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ForgotFrg.viewModel
        }
    }

    override fun initViews() {
        viewBinding.btConfirm.setOnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val rs = viewModel.validate(email)
            if (rs == ForgotVM.ERROR_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else if (rs == ForgotVM.INVALID_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else {
                viewModel.forgotPassword(email, ::forgotSuccess, ::emailInvalid)
            }
        }
        viewBinding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun forgotSuccess() {
        context?.showToastLong(getString(R.string.text_send_to_email))
        findNavController().popBackStack()
    }

    private fun emailInvalid() {
        context?.showToastShort(getString(R.string.error_something_wrong))
    }
}