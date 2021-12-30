package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalPageVM @Inject constructor() : BaseViewModel() {
    private val user: User? = null

    fun getInformation(
        loadingSuccess: (mEmail: Any?, mMame: Any?, mPhoto: Any?) -> Unit,
    ) {
        mUser?.uid?.let {
            root?.database?.reference?.child(USERS)?.child(it)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val email = task.result.child(EMAIL).value
                        val name = task.result.child(NAME).value
                        val photo = task.result.child(PHOTO).value
                        user?.Email = email.toString()
                        user?.Name = name.toString()
                        user?.PhotoUrl = photo.toString()
                        loadingSuccess(email, name, photo)
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
        private const val PHOTO: String = "PhotoUrl"
    }
}
