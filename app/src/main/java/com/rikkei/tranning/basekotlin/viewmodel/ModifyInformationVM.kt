package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModifyInformationVM @Inject constructor() : BaseViewModel() {
    private val user: User? = null

    fun getInformation(action: (mMame: Any?, mPhone: Any?, mDate: Any?) -> Unit) {

        mUser?.uid?.let {
            databaseReference?.database?.reference?.child(USERS)?.child(it)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val name = task.result.child(NAME).value
                        val phone = task.result.child(PHONE).value
                        val date = task.result.child(DATE).value
                        user?.name = name.toString()
                        user?.phone = phone.toString()
                        user?.dob = date.toString()
                        action(name, phone, date)
                    }
                }
        }
    }

    fun updateProfile(name: String, phone: String, date: String) {
        mUser?.uid?.let { databaseReference?.child(USERS)?.child(it)?.push()?.key }
            ?: return
        val childUpdate = hashMapOf<String, Any>(
            "$USERS/${mUser!!.uid}/$NAME" to name,
            "$USERS/${mUser!!.uid}/$PHONE" to phone,
            "$USERS/${mUser!!.uid}/$DATE" to date
        )
        databaseReference?.updateChildren(childUpdate)
    }

    companion object {
        private const val USERS: String = "Users"
        private const val NAME: String = "Name"
        private const val DATE: String = "Date of birth"
        private const val PHONE: String = "Phone"
        private const val PHOTO: String = "PhotoUrl"
    }
}