package com.rikkei.tranning.basekotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ChatRoomVM @Inject constructor() : BaseViewModel() {
    val liveListChats: MutableLiveData<List<ChatMessage>> by lazy {
        MutableLiveData<List<ChatMessage>>(
            ArrayList()
        )
    }
    val listChat = ArrayList<ChatMessage>()
    val senderID: String? = mUser?.uid

    fun sendMessage(sender: String, receiver: String, message: String, time: String, date: String) {
        val hashmap = hashMapOf<String, Any>(
            "SenderID" to sender,
            "ReceiverID" to receiver,
            "Time" to time,
            "Date" to date,
            "Message" to message
        )
        root?.database?.reference?.child("Chats")?.push()?.setValue(hashmap)
    }

    fun getData(
        myId: String,
        userId: String,
        imageUrl: String,
        actionSetText: (mDate: String) -> Unit,
        actionToday: () -> Unit,
    ) {
        val getListChat = root?.database?.reference?.child("Chats")
        val date = DateFormat.getDateInstance().format(Date())
        getListChat?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val chat: ChatMessage? = postSnapshot.getValue<ChatMessage>()!!
                    if (chat?.ReceiverID.equals(myId) && chat?.SenderID.equals(userId) || chat?.ReceiverID.equals(
                            userId
                        ) && chat?.SenderID.equals(myId)
                    ) {
                        chat?.let { listChat.add(it) }
                    }
                    if (chat?.Date == date) {
                        actionToday()
                    } else {
                        actionSetText(date)
                    }
                    Log.d("Thang", "onDataChange: ${chat?.Date}")
                }
                liveListChats.postValue(listChat)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }
}