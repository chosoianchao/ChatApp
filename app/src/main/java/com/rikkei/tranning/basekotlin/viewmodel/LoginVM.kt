package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor() : BaseViewModel() {
    private var user: User? = null

    fun validate(email: String, password: String): Int {
        user?.email = email
        user?.password = password
        if (email.isEmpty()) {
            return ERROR_EMAIL
        } else if (!isEmailInvalid(email)) {
            return INVALID_EMAIL
        } else if (password.isEmpty() || password.length <= 5) {
            return ERROR_PASSWORD
        } else {
            return SUCCESS
        }
    }

    fun login(
        email: String,
        password: String,
        actionSuccess: () -> Unit,
        actionFailed: () -> Unit
    ) {
        user?.email = email
        user?.password = password

        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                actionSuccess()
            }
        }?.addOnFailureListener {
            actionFailed()
        }
    }

    fun accountExists(action: () -> Unit) {
        if (mUser != null) {
            action()
        }
    }

    companion object {
        const val INVALID_EMAIL: Int = 401
        const val ERROR_EMAIL: Int = 404
        const val ERROR_PASSWORD: Int = 402
        const val SUCCESS: Int = 201
    }
}
