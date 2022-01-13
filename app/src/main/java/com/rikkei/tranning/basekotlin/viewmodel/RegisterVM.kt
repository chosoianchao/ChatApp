package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor() : BaseViewModel() {
    private val user: User? = null

    fun validate(name: String, email: String, password: String): Int {
        user?.Name = name
        user?.Email = email
        user?.Password = password
        return if (name.isEmpty()) {
            ERROR_NAME
        } else if (email.isEmpty()) {
            ERROR_EMAIL
        } else if (!isEmailInvalid(email)) {
            INVALID_EMAIL
        } else if (password.isEmpty() || password.length <= 5) {
            ERROR_PASSWORD
        } else {
            SUCCESS
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        actionSuccess: () -> Unit,
        actionFailed: () -> Unit
    ) {
        user?.Name = name
        user?.Email = email
        user?.Password = password

        auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                actionSuccess()
                insertData(name, email)
            }
        }?.addOnFailureListener {
            actionFailed()
        }
    }

    private fun insertData(name: String, email: String) {
        val ref = mUser?.uid?.let { root?.database?.reference?.child("Users")?.child(it) }
        ref?.child("Id")?.setValue(mUser?.uid)
        ref?.child("Name")?.setValue(name)
        ref?.child("Email")?.setValue(email)
        ref?.child("Phone")?.setValue("")
        ref?.child("Date")?.setValue("")
        ref?.child("Description")?.setValue("")
        ref?.child("PhotoUrl")?.setValue("")
        ref?.child("Password")?.setValue("")
    }

    companion object {
        internal const val INVALID_EMAIL: Int = 401
        internal const val ERROR_PASSWORD: Int = 402
        internal const val ERROR_NAME: Int = 403
        internal const val ERROR_EMAIL: Int = 404
        internal const val SUCCESS: Int = 201
    }
}