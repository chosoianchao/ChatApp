package com.rikkei.tranning.basekotlin.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgLoginBinding
import com.rikkei.tranning.basekotlin.showToastLong
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.LoginVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFrg : BaseFragment<FrgLoginBinding>() {
    var action: NavDirections? = null

    override val layoutResource: Int
        get() = R.layout.frg_login

    override val viewModel: LoginVM by viewModels()

    override fun onStart() {
        super.onStart()
        viewModel.accountExists(::accountExists)
    }

    override fun initViews() {
        getBackStackData("bundleKey", ::onResult)
        viewBinding.buttonLogin.setOnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val password = viewBinding.editPassword.text.toString()
            when (viewModel.validate(email, password)) {
                LoginVM.ERROR_EMAIL -> {
                    context?.showToastShort(getString(R.string.text_validate_email))
                }
                LoginVM.INVALID_EMAIL -> {
                    context?.showToastShort(getString(R.string.text_write_valid_email))
                }
                LoginVM.ERROR_PASSWORD -> {
                    context?.showToastShort(getString(R.string.text_password_validate))
                }
                else -> {
                    viewModel.login(email, password, ::loginSuccess, ::loginFailed, ::validateEmail)
                }
            }
        }
        viewBinding.textForgetPassword.setOnClickListener {
            openForgotPasswordFragment()
        }
        viewBinding.textSignupNow.setOnClickListener {
            openRegisterFragment()
        }
    }

    private fun validateEmail() {
        context?.showToastLong(getString(R.string.text_validated_email))
    }

    private fun accountExists() {
        action = LoginFrgDirections.actionLoginFrgToMainChatFrg()
        findNavController().navigate(action!!)
    }

    private fun onResult(bundle: Bundle) {
        val m = bundle.getString("email")
        val p = bundle.getString("password")
        viewBinding.editEmail.setText(m)
        viewBinding.editPassword.setText(p)
    }

    private fun loginFailed() {
        context?.showToastShort(getString(R.string.error_something_wrong))
    }

    private fun openRegisterFragment() {
        action = LoginFrgDirections.actionLoginFrgToRegisterFrg()
        findNavController().navigate(action!!)
    }

    private fun openForgotPasswordFragment() {
        action = LoginFrgDirections.actionLoginFrgToForgotFrg()
        findNavController().navigate(action!!)
    }

    private fun loginSuccess() {
        context?.showToastShort(getString(R.string.success_login))
        action = LoginFrgDirections.actionLoginFrgToMainChatFrg()
        findNavController().navigate(action!!)
    }

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@LoginFrg.viewModel
        }
    }
}