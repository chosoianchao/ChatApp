package com.rikkei.tranning.basekotlin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FragmentLoginBinding
import com.rikkei.tranning.basekotlin.model.User
import com.rikkei.tranning.basekotlin.showToastShort
import com.rikkei.tranning.basekotlin.viewmodel.LoginModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutResource: Int
        get() = R.layout.fragment_login

    override val viewModel: LoginModel by viewModels()

    override fun initViews() {

        getBackStackData("bundleKey", ::onResult)
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
        viewModel.readData()
    }

    private fun readData(postReference: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<User>()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
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
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun openForgotPasswordFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToForgotFragment()
        findNavController().navigate(action)
    }

    private fun loginSuccess() {
        context?.showToastShort(getString(R.string.success_login))
//        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
//        findNavController().navigate(action)
    }

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@LoginFragment.viewModel
        }
    }
}