package com.rikkei.tranning.basekotlin.viewmodel

import androidx.lifecycle.viewModelScope
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor() : BaseViewModel() {
    private val user: User? = null

    fun validate(name: String, email: String, password: String): Int {
        user?.name = name
        user?.email = email
        user?.password = password
        if (name.isEmpty()) {
            return ERROR_NAME
        } else if (email.isEmpty()) {
            return ERROR_EMAIL
        } else if (!isEmailInvalid(email)) {
            return INVALID_EMAIL
        } else if (password.isEmpty() || password.length <= 5) {
            return ERROR_PASSWORD
        } else {
            return SUCCESS
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        actionSuccess: () -> Unit,
        actionFailed: () -> Unit,
    ) {
        user?.name = name
        user?.email = email
        user?.password = password

        auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                actionSuccess()
                viewModelScope.launch(Dispatchers.IO) {
                    insertData(name, email)
                }
            }
        }?.addOnFailureListener {
            actionFailed()
        }
    }

    private fun insertData(name: String, email: String) {
        val mRef = mUser?.uid?.let {
            databaseReference?.database?.reference?.child(USERS)?.child(it)
        }
        mRef?.child(NAME)?.setValue(name)
        mRef?.child(EMAIL)?.setValue(email)
    }

    companion object {
        const val INVALID_EMAIL: Int = 401
        const val ERROR_PASSWORD: Int = 402
        const val ERROR_NAME: Int = 403
        const val ERROR_EMAIL: Int = 404
        const val SUCCESS: Int = 201
        private const val USERS: String = "Users"
        private const val EMAIL: String = "Email"
        private const val NAME: String = "Name"
    }
}