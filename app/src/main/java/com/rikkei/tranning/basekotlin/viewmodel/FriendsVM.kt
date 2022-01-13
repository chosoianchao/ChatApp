package com.rikkei.tranning.basekotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendsVM @Inject constructor() : BaseViewModel() {
    val liveListFriends: MutableLiveData<List<Friends>> by lazy {
        MutableLiveData<List<Friends>>(
            ArrayList()
        )
    }
    val liveListSearch: MutableLiveData<List<Friends>> by lazy {
        MutableLiveData<List<Friends>>(
            ArrayList()
        )
    }

    fun getData(msg: String) {
        val getListFriends = mUser?.uid?.let { root?.child("Friends")?.child(it) }
        getListFriends?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (msg == "") {
                    val listFriends = ArrayList<Friends>()
                    for (postSnapshot in snapshot.children) {
                        val friends: Friends? = postSnapshot.getValue<Friends>()
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

    fun searchFriends(p0: String?) {
        val query: Query? =
            mUser?.let {
                FirebaseDatabase.getInstance().getReference("Friends").child(it.uid)
                    .orderByChild("Name")
                    .startAt(p0).endAt(p0 + "\uf8ff")
            }
        query?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listFriends = ArrayList<Friends>()
                for (postSnapshot in snapshot.children) {
                    val friends: Friends? = postSnapshot.getValue<Friends>()
                    if (!friends?.Id.equals(mUser?.uid)) {
                        friends?.let { listFriends.add(it) }
                    }
                }
                liveListSearch.postValue(listFriends)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }
}
