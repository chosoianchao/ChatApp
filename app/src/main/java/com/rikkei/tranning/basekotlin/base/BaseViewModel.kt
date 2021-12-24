package com.rikkei.tranning.basekotlin.base

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

abstract class BaseViewModel : ViewModel() {

    protected val auth: FirebaseAuth? by lazy { Firebase.auth }

    protected val databaseReference: DatabaseReference? by lazy { FirebaseDatabase.getInstance().reference }

    protected val mUser: FirebaseUser? by lazy { auth?.currentUser }

    fun isEmailInvalid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
