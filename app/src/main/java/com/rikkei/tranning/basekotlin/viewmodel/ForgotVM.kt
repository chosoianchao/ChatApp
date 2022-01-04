package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotVM @Inject constructor() : BaseViewModel() {

    private val user: User? = null
    fun validate(email: String): Int {
        user?.Email = email
        return if (email.isEmpty()) {
            ERROR_EMAIL
        } else if (!isEmailInvalid(email)) {
            INVALID_EMAIL
        } else {
            SUCCESS
        }
    }

    fun forgotPassword(email: String, actionSuccess: () -> Unit, emailInvalid: () -> Unit) {
        user?.Email = email
        if (mUser?.isEmailVerified == true) {
            auth?.sendPasswordResetEmail(email)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    actionSuccess()
                }
            }
        } else {
            emailInvalid()
        }
    }

    companion object {
        internal const val SUCCESS: Int = 201
        internal const val INVALID_EMAIL: Int = 401
        internal const val ERROR_EMAIL: Int = 404
    }
}