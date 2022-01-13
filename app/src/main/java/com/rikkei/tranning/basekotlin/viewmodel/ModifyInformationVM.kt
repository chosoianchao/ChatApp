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
            root?.database?.reference?.child("Users")?.child(it)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val name = task.result.child("Name").value
                        val phone = task.result.child("Phone").value
                        val date = task.result.child("Date").value
                        val photo = task.result.child("PhotoUrl").value
                        val desc = task.result.child("Description").value
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

        root?.child("Users")?.push()?.key ?: return

        val ref = reference.child("image/$photo.jpg")

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
                    "Users/${mUser?.uid}/Name" to name,
                    "Users/${mUser?.uid}/Phone" to phone,
                    "Users/${mUser?.uid}/Date" to date,
                    "Users/${mUser?.uid}/Description" to desc,
                    "Users/${mUser?.uid}/PhotoUrl" to downloadUri.toString()
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
        root?.child("Users")?.push()?.key ?: return
        val childUpdate = hashMapOf<String, Any>(
            "Users/${mUser?.uid}/Name" to name,
            "Users/${mUser?.uid}/Phone" to phone,
            "Users/${mUser?.uid}/Date" to date,
            "Users/${mUser?.uid}/Description" to desc
        )
        root?.updateChildren(childUpdate)?.addOnCompleteListener {
            action()
        }
    }
}