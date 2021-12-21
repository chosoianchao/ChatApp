package com.rikkei.tranning.basekotlin.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgLoginBinding
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.LoginVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFrg : BaseFragment<FrgLoginBinding>() {

    override val layoutResource: Int
        get() = R.layout.frg_login

    override val viewModel: LoginVM by viewModels()

    override fun initViews() {
        viewModel.accountExists(::accountExists)

        getBackStackData(BUNDLE_KEY, ::onResult)
        viewBinding.buttonLogin.setOnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val password = viewBinding.editPassword.text.toString()
            val rs = viewModel.validate(email, password)
            if (rs == LoginVM.ERROR_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else if (rs == LoginVM.INVALID_EMAIL) {
                context?.showToastShort(getString(R.string.text_write_valid_email))
            } else if (rs == LoginVM.ERROR_PASSWORD) {
                context?.showToastShort(getString(R.string.text_password_validate))
            } else {
                viewModel.login(email, password, ::loginSuccess, ::loginFailed)
            }
        }
        viewBinding.textForgetPassword.setOnClickListener {
            openForgotPasswordFragment()
        }
        viewBinding.textSignupNow.setOnClickListener {
            openRegisterFragment()
        }
    }

    private fun accountExists() {
        val action = LoginFrgDirections.actionLoginFrgToMainChatFrg()
        findNavController().navigate(action)
    }

    private fun onResult(bundle: Bundle) {
        val m = bundle.getString(EMAIL)
        val p = bundle.getString(PASSWORD)
        viewBinding.editEmail.setText(m)
        viewBinding.editPassword.setText(p)
    }

    private fun loginFailed() {
        context?.showToastShort(getString(R.string.error_something_wrong))
    }

    private fun openRegisterFragment() {
        val action = LoginFrgDirections.actionLoginFrgToRegisterFrg()
        findNavController().navigate(action)
    }

    private fun openForgotPasswordFragment() {
        val action = LoginFrgDirections.actionLoginFrgToForgotFrg()
        findNavController().navigate(action)
    }

    private fun loginSuccess() {
        context?.showToastShort(getString(R.string.success_login))
        val action = LoginFrgDirections.actionLoginFrgToMainChatFrg()
        findNavController().navigate(action)
    }

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@LoginFrg.viewModel
        }
    }

    companion object {
        private const val EMAIL: String = "email"
        private const val BUNDLE_KEY: String = "bundleKey"
        private const val PASSWORD: String = "password"
    }
}