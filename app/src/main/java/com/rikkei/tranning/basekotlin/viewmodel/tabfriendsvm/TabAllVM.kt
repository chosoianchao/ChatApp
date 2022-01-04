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
class TabAllVM @Inject constructor() : BaseViewModel() {
    val liveListRequest: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>(ArrayList()) }

    fun getData() {
        val getListRequest = root?.child(USERS)
        getListRequest?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listUser = ArrayList<User>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.child(EMAIL).value != mUser?.email) {
                            val user: User? = postSnapshot.getValue<User>()
                            user?.let { listUser.add(it) }
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

    companion object {
        private const val USERS: String = "Users"
        private const val EMAIL: String = "Email"
    }
}