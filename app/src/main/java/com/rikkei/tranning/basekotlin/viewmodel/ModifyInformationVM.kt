package com.rikkei.tranning.basekotlin.viewmodel

import android.net.Uri
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ModifyInformationVM @Inject constructor() : BaseViewModel() {
    private var user: User? = null

    fun getInformation(action: (mMame: Any?, mPhone: Any?, mDate: Any?, mPhoto: Any?, mDesc: Any?) -> Unit) {
        mUser?.uid?.let {
            root?.database?.reference?.child(USERS)?.child(it)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val name = task.result.child(NAME).value
                        val phone = task.result.child(PHONE).value
                        val date = task.result.child(DATE).value
                        val photo = task.result.child(PHOTO).value
                        val desc = task.result.child(DESC).value
                        user?.Name = name.toString()
                        user?.Phone = phone.toString()
                        user?.Date = date.toString()
                        user?.PhotoUrl = photo.toString()
                        action(name, phone, date, photo, desc)
                    }

                }
        }
    }

    fun updateProfile(
        name: String,
        phone: String,
        date: String,
        photo: Uri,
        desc: String,
        loadingSuccess: () -> Unit
    ) {
        user?.Name = name
        user?.Phone = phone
        user?.Date = date
        user?.PhotoUrl = photo.toString()

        root?.child(USERS)?.push()?.key ?: return

        val ref = reference.child("$PATH$photo.jpg")

        val uploadTask = ref.putFile(photo)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val childUpdate = hashMapOf<String, Any>(
                    "$USERS/${mUser?.uid}/$NAME" to name,
                    "$USERS/${mUser?.uid}/$PHONE" to phone,
                    "$USERS/${mUser?.uid}/$DATE" to date,
                    "$USERS/${mUser?.uid}/$DESC" to desc,
                    "$USERS/${mUser?.uid}/$PHOTO" to downloadUri.toString()
                )
                root?.updateChildren(childUpdate)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        loadingSuccess()
                    }
                }
            }
        }
    }

    fun upload(name: String, phone: String, date: String, desc: String, action: () -> Unit) {
        root?.child(USERS)?.push()?.key ?: return
        val childUpdate = hashMapOf<String, Any>(
            "$USERS/${mUser?.uid}/$NAME" to name,
            "$USERS/${mUser?.uid}/$PHONE" to phone,
            "$USERS/${mUser?.uid}/$DATE" to date,
            "$USERS/${mUser?.uid}/$DESC" to desc
        )
        root?.updateChildren(childUpdate)?.addOnCompleteListener {
            action()
        }
    }

    companion object {
        private const val USERS: String = "Users"
        private const val NAME: String = "Name"
        private const val DATE: String = "Date"
        private const val DESC: String = "Description"
        private const val PHONE: String = "Phone"
        private const val PHOTO: String = "PhotoUrl"
        private const val PATH: String = "image/"
    }
}