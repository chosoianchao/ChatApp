package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor() : BaseViewModel() {
    private var user: User? = null

    fun validate(email: String, password: String): Int {
        user?.Email = email
        user?.Password = password
        return if (email.isEmpty()) {
            ERROR_EMAIL
        } else if (!isEmailInvalid(email)) {
            INVALID_EMAIL
        } else if (password.isEmpty() || password.length <= 5) {
            ERROR_PASSWORD
        } else {
            SUCCESS
        }
    }

    fun login(
        email: String,
        password: String,
        actionSuccess: () -> Unit,
        actionValidateEmail: () -> Unit,
        actionFailed: () -> Unit
    ) {
        user?.Email = email
        user?.Password = password

        // if (mUser?.isEmailVerified == true) {
        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                actionSuccess()
            }
        }?.addOnFailureListener {
            actionFailed()
        }
        //  }
//        else{
//            actionValidateEmail
//        }
    }

    fun accountExists(action: () -> Unit) {
        if (mUser != null) {
            action()
        }
    }

    companion object {
        internal const val INVALID_EMAIL: Int = 401
        internal const val ERROR_EMAIL: Int = 404
        internal const val ERROR_PASSWORD: Int = 402
        internal const val SUCCESS: Int = 201
    }
}
