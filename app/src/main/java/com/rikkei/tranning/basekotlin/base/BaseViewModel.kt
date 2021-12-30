package com.rikkei.tranning.basekotlin.base

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class BaseViewModel : ViewModel() {

    protected val auth: FirebaseAuth? by lazy { FirebaseAuth.getInstance() }

    protected val root: DatabaseReference? by lazy { FirebaseDatabase.getInstance().reference }

    protected val mUser: FirebaseUser? by lazy { auth?.currentUser }

    protected val reference: StorageReference by lazy { FirebaseStorage.getInstance().reference }

    fun isEmailInvalid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
