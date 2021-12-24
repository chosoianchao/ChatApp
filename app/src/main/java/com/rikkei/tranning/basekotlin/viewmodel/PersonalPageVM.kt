package com.rikkei.tranning.basekotlin.viewmodel

import androidx.lifecycle.viewModelScope
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalPageVM @Inject constructor() : BaseViewModel() {
    private val user: User? = null
    fun readInformation(action: (mEmail: Any?, mMame: Any?) -> Unit) {
        if (mUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                mUser?.uid?.let {
                    databaseReference?.database?.reference?.child(USERS)?.child(it)?.get()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val email = task.result.child(EMAIL).value
                                val name = task.result.child(NAME).value
                                user?.email = email.toString()
                                user?.name = name.toString()
                                action(email, name)
                            }
                        }
                }
            }
        }
    }

    fun logOutAccount() {
        auth?.signOut()
    }

    companion object {
        private const val EMAIL: String = "Email"
        private const val NAME: String = "Name"
        private const val USERS: String = "Users"
    }
}
