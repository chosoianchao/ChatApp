package com.rikkei.tranning.basekotlin.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FragmentForgetPasswordBinding
import com.rikkei.tranning.basekotlin.showToastLong
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.ForgotViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotFragment : BaseFragment<FragmentForgetPasswordBinding>() {
    override val layoutResource: Int
        get() = R.layout.fragment_forget_password

    override val viewModel: ForgotViewModel by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ForgotFragment.viewModel
        }
    }

    override fun initViews() {
        viewBinding.btConfirm.setOnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val rs = viewModel.validate(email)
            if (rs == ForgotViewModel.ERROR_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else if (rs == ForgotViewModel.INVALID_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else {
                viewModel.forgotPassword(email, ::forgotSuccess, ::forgotFailed)
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

    private fun forgotFailed() {
        context?.showToastShort(getString(R.string.error_something_wrong))
    }
}