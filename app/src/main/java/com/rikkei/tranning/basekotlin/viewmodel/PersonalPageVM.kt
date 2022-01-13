package com.rikkei.tranning.basekotlin.viewmodel

import com.google.firebase.messaging.FirebaseMessaging
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
            root?.database?.reference?.child("Users")?.child(it)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val email = task.result.child("Email").value
                        val name = task.result.child("Name").value
                        val photo = task.result.child("PhotoUrl").value
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

    fun enableNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/${mUser?.uid}")
    }

    fun disableNotification() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/${mUser?.uid}")
    }
}
