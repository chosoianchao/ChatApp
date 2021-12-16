package com.rikkei.tranning.basekotlin.viewmodel

import androidx.lifecycle.viewModelScope
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotViewModel @Inject constructor() : BaseViewModel() {

    private val user: User? = null
    fun validate(email: String): Int {
        user?.email = email
        if (email.isEmpty()) {
            return ERROR_EMAIL
        } else if (!isEmailInvalid(email)) {
            return INVALID_EMAIL
        } else {
            return SUCCESS
        }
    }

    fun forgotPassword(email: String, actionSuccess: () -> Unit, actionFailed: () -> Unit) {
        user?.email = email
        viewModelScope.launch(Dispatchers.IO) {
            auth?.sendPasswordResetEmail(email)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    actionSuccess()
                }
            }?.addOnFailureListener {
                actionFailed()
            }
        }
    }

    companion object {
        const val SUCCESS: Int = 201
        const val INVALID_EMAIL: Int = 401
        const val ERROR_EMAIL: Int = 404
    }
}