package com.rikkei.tranning.basekotlin.fragment

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FragmentLoginBinding
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.LoginModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutResource: Int
        get() = R.layout.fragment_login

    override val viewModel: LoginModel by viewModels()

    override fun initViews() {
//        val window: Window = requireActivity().window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.white)
        viewModel.accountExists(::accountExists)

        getBackStackData(BUNDLE_KEY, ::onResult)
        viewBinding.buttonLogin.setOnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val password = viewBinding.editPassword.text.toString()
            val rs = viewModel.validate(email, password)
            if (rs == LoginModel.ERROR_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else if (rs == LoginModel.INVALID_EMAIL) {
                context?.showToastShort(getString(R.string.text_write_valid_email))
            } else if (rs == LoginModel.ERROR_PASSWORD) {
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
        val action = LoginFragmentDirections.actionLoginFragmentToMainChatFragment()
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
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun openForgotPasswordFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToForgotFragment()
        findNavController().navigate(action)
    }

    private fun loginSuccess() {
        context?.showToastShort(getString(R.string.success_login))
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@LoginFragment.viewModel
        }
    }

    companion object {
        private const val EMAIL: String = "email"
        private const val BUNDLE_KEY: String = "bundleKey"
        private const val PASSWORD: String = "password"
    }
}