package com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabFriendsVM @Inject constructor() : BaseViewModel() {
    val liveListFriends: MutableLiveData<List<Friends>> by lazy {
        MutableLiveData<List<Friends>>(
            ArrayList()
        )
    }

    fun getData() {
        val getListFriends = mUser?.uid?.let { root?.child(FRIENDS)?.child(it) }
        getListFriends?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listFriends = ArrayList<Friends>()
                    for (postSnapshot in snapshot.children) {
                        val friends: Friends? = postSnapshot.getValue<Friends>()
                        Log.d("thang", "onDataChange: $friends")
                        friends?.let { listFriends.add(it) }
                    }
                    liveListFriends.postValue(listFriends)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    companion object {
        private const val FRIENDS: String = "Friends"
    }
}