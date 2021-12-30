package com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm

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

    private lateinit var currentState: String

    val liveListRequest: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>(ArrayList()) }

    fun getData() {
        val getListRequest = root?.child(USERS)
        getListRequest?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listUser = ArrayList<User>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.child(EMAIL).value != mUser?.email) {
                            val user: User? = postSnapshot.getValue<User>()!!
                            user?.let { listUser.add(it) }
                            currentState = NOT_FRIENDS
                        }
                    }
                    liveListRequest.postValue(listUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    fun addFriends(data: Any?) {
        val user: User = data as User
        if (currentState == NOT_FRIENDS) {
            mUser?.uid?.let { send ->
                user.Id.let { receive ->
                    root?.database?.reference?.child(FRIENDS)
                        ?.child(send)
                        ?.child(receive)?.child(REQUEST_TYPE)?.setValue(SENT)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                root!!.database.reference
                                    .child(FRIENDS)
                                    .child(receive)
                                    .child(send)
                                    .child(REQUEST_TYPE)
                                    .setValue(RECEIVED).addOnSuccessListener {
                                        currentState = REQUEST_SENT
                                    }
                            }
                        }
                }
            }
        }
    }

    companion object {
        private const val USERS: String = "Users"
        private const val EMAIL: String = "Email"
        private const val NOT_FRIENDS: String = "not_friends"
        private const val REQUEST_SENT: String = "req_sent"
        private const val REQUEST_TYPE: String = "request_type"
        private const val FRIENDS: String = "Friends"
        private const val RECEIVED: String = "received"
        private const val SENT: String = "sent"
    }
}