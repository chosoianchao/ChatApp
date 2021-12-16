package com.rikkei.tranning.basekotlin.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginModel @Inject constructor() : BaseViewModel() {
    private var user: User? = null

    init {
        databaseReference?.child("user")?.child("username")?.setValue("ahihi")

    }

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

        viewModelScope.launch(Dispatchers.IO) {
            auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Thang", "login: success" + task.result)
                    actionSuccess()
                }
            }?.addOnFailureListener {
                actionFailed()
                Log.d("Thang", "login: failed" + it.message)
            }
        }
    }

    fun readData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<User>()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        databaseReference?.addValueEventListener(postListener)
    }

    companion object {
        val TAG: String = LoginModel::javaClass.name
        const val INVALID_EMAIL: Int = 401
        const val ERROR_EMAIL: Int = 404
        const val ERROR_PASSWORD: Int = 402
        const val SUCCESS: Int = 201
    }
}
