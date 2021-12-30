package com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabRequestVM @Inject constructor() : BaseViewModel() {

    private var user: User? = null

    var liveUser: MutableLiveData<List<User>> =
        MutableLiveData<List<User>>(ArrayList())

    fun getData() {
        val myTopPostsQuery = root?.child(USERS)
        myTopPostsQuery?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listUser = ArrayList<User>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.child(EMAIL).value.toString() != mUser?.email) {
                            user = postSnapshot.getValue<User>()
                            user?.let { listUser.add(it) }
                            CURRENT_STATE = "not_friends"
                            Log.d(
                                "Thang",
                                "onDataChange() called with: liveUser = ${liveUser.value}  + \n$user"
                            )
                        }
                    }
                    liveUser.postValue(listUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    fun addFriends() {
        Log.d("Thang", "addFriends() called ${user?.Id}")
        if (CURRENT_STATE == "not_friends") {
            mUser?.email?.let { send ->
                user?.Email?.let { receive ->
                    root?.database?.reference?.child(USERS)?.child(send)?.child("Friends")
                        ?.child(send)
                        ?.child(receive)?.child("request_type")?.setValue("sent")
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                root!!.database.reference.child(USERS).child(send)
                                    .child("Friends")
                                    .child(receive)
                                    .child(send)
                                    .child("request_type")
                                    .setValue("received").addOnSuccessListener {
                                        CURRENT_STATE = "req_sent"
                                        Log.d("Thang", "addFriends() called : Success")
                                    }
                            }
                        }
                }
            }
        } else {

            Log.d("Thang", "addFriends() called: Failed Sending Request")
        }
    }

    companion object {
        private const val USERS: String = "Users"
        private const val EMAIL: String = "Email"
        private var CURRENT_STATE: String = ""
    }
}