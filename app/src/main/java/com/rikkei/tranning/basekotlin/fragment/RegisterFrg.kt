package com.rikkei.tranning.basekotlin.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgRegisterBinding
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.RegisterVM
import com.rikkei.tranning.basekotlin.viewmodel.RegisterVM.Companion.ERROR_EMAIL
import com.rikkei.tranning.basekotlin.viewmodel.RegisterVM.Companion.ERROR_NAME
import com.rikkei.tranning.basekotlin.viewmodel.RegisterVM.Companion.ERROR_PASSWORD
import com.rikkei.tranning.basekotlin.viewmodel.RegisterVM.Companion.INVALID_EMAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFrg : BaseFragment<FrgRegisterBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_register

    override val viewModel: RegisterVM by viewModels()


    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@RegisterFrg.viewModel
        }
    }

    override fun initViews() {
        viewBinding.buttonRegister.setOnClickListener {
            val name = viewBinding.editName.text.toString()
            val email = viewBinding.editEmail.text.toString()
            val password = viewBinding.editPassword.text.toString()
            when (viewModel.validate(name, email, password)) {
                ERROR_NAME -> {
                    context?.showToastShort(getString(R.string.text_validate_name))
                }
                ERROR_EMAIL -> {
                    context?.showToastShort(getString(R.string.text_validate_email))
                }
                INVALID_EMAIL -> {
                    context?.showToastShort(getString(R.string.text_write_valid_email))
                }
                ERROR_PASSWORD -> {
                    context?.showToastShort(getString(R.string.text_password_validate))
                }
                else -> {
                    viewModel.register(
                        name,
                        email,
                        password,
                        ::registerSuccess,
                        ::registerFailed
                    )
                }
            }
        }

        viewBinding.textLoginNow.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun registerSuccess() {
        val email = viewBinding.editEmail.text.toString()
        val password = viewBinding.editPassword.text.toString()
        context?.showToastShort(getString(R.string.success_register))
        val bundle: Bundle = Bundle().apply {
            putString("email", email)
            putString("password", password)
        }
        setBackStackData("bundleKey", bundle, true)
    }

    private fun registerFailed() {
        context?.showToastShort(getString(R.string.error_something_wrong))
    }
}