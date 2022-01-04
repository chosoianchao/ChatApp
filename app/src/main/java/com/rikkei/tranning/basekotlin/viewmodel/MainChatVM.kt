package com.rikkei.tranning.basekotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.ChatMessage
import com.rikkei.tranning.basekotlin.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainChatVM @Inject constructor() : BaseViewModel() {
    val liveListFriends: MutableLiveData<List<Friends>> by lazy {
        MutableLiveData<List<Friends>>(
            ArrayList()
        )
    }
    val listFriends = ArrayList<Friends>()
    val friendsChat = ArrayList<String>()
    fun getData() {
        val ref = root?.database?.reference?.child("Chats")
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val chat: ChatMessage? = postSnapshot.getValue<ChatMessage>()
                    if (chat?.SenderID.equals(mUser?.uid)) {
                        chat?.ReceiverID?.let { friendsChat.add(it) }
                    } else if (chat?.ReceiverID.equals(mUser?.uid)) {
                        chat?.SenderID?.let { friendsChat.add(it) }
                    }
                }
                readChat()
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    private fun readChat() {
        val getList = mUser?.uid?.let { root?.database?.reference?.child("Friends")?.child(it) }
        getList?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                handleReadChat(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                throw  error.toException()
            }
        })
    }

    private fun handleReadChat(snapshot: DataSnapshot){
        for (postSnapshot in snapshot.children) {
            val friends: Friends? = postSnapshot.getValue<Friends>()
            for (id in friendsChat) {
                if (friends?.Id.equals(id)) {
                    if (listFriends.size != 0) {
                        val listFriendsAdded = ArrayList<Friends>()
                        for (friends1 in listFriends) {
                            if (!friends?.Id.equals(friends1.Id)) {
                                friends?.let { listFriendsAdded.add(it) }
                            }
                        }
                        listFriends.addAll(listFriendsAdded)
                    } else {
                        friends?.let { listFriends.add(it) }
                    }
                    liveListFriends.postValue(listFriends)
                }
            }
            Log.d("Thang", "listFriends: $friends")
            Log.d("Thang", "chat: $friendsChat")
        }
    }
}