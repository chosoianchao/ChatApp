package com.rikkei.tranning.basekotlin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FragmentRegisterBinding
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.RegisterModel
import com.rikkei.tranning.basekotlin.viewmodel.RegisterModel.Companion.ERROR_EMAIL
import com.rikkei.tranning.basekotlin.viewmodel.RegisterModel.Companion.ERROR_NAME
import com.rikkei.tranning.basekotlin.viewmodel.RegisterModel.Companion.ERROR_PASSWORD
import com.rikkei.tranning.basekotlin.viewmodel.RegisterModel.Companion.INVALID_EMAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val layoutResource: Int
        get() = R.layout.fragment_register

    override val viewModel: RegisterModel by viewModels()



    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@RegisterFragment.viewModel
        }
    }

    override fun initViews() {
        viewBinding.buttonRegister.setOnClickListener {
             val name = viewBinding.editName.text.toString()
             val email = viewBinding.editEmail.text.toString()
             val password = viewBinding.editPassword.text.toString()
            val rs = viewModel.validate(name, email, password)
            if (rs == ERROR_NAME) {
                context?.showToastShort(getString(R.string.text_validate_name))
            } else if (rs == ERROR_EMAIL) {
                context?.showToastShort(getString(R.string.text_validate_email))
            } else if (rs == INVALID_EMAIL) {
                context?.showToastShort(getString(R.string.text_write_valid_email))
            } else if (rs == ERROR_PASSWORD) {
                context?.showToastShort(getString(R.string.text_password_validate))
            } else {
                viewModel.register(name, email, password, ::registerSuccess, ::registerFailed)
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