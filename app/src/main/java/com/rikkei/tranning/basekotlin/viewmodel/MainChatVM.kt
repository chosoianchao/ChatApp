package com.rikkei.tranning.basekotlin.viewmodel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainChatVM @Inject constructor() : BaseViewModel() {

    private val mConvDatabase =
        mUser?.uid?.let { root?.database?.reference?.child("Chat")?.child(it) }
    val mMessageDatabase =
        mUser?.uid?.let { root?.database?.reference?.child("messages")?.child(it) }
    val mFriendsDatabase =
        mUser?.uid?.let { root?.database?.reference?.child("Friends")?.child(it) }
    val conversationQuery: Query? = mConvDatabase?.orderByChild("timestamp")

    fun remove(friends: Friends?) {
        val query: Query = friends?.let { mMessageDatabase?.child(it.Id) }!!
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    postSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}
