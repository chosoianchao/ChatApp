package com.rikkei.tranning.basekotlin.viewmodel

import android.util.Log
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterModel @Inject constructor() : BaseViewModel() {
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
                updateData(email)
            }
        }?.addOnFailureListener {
            actionFailed()
        }
    }

    private fun updateData(email: String) {
        val currentID = auth?.currentUser?.uid
        if (currentID != null) {
            databaseReference?.child("user")?.child(currentID)?.child("username")?.setValue(email)?.addOnCompleteListener {
                Log.d("TAG", "updateData: ")
            }?.addOnFailureListener {
                Log.d("TAG", "updateData: ")

            }
        }
    }

    companion object {
        const val INVALID_EMAIL: Int = 401
        const val ERROR_PASSWORD: Int = 402
        const val ERROR_NAME: Int = 403
        const val ERROR_EMAIL: Int = 404
        const val SUCCESS: Int = 201
    }
}