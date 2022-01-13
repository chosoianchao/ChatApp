package com.rikkei.tranning.basekotlin.viewmodel

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.Friends
import com.rikkei.tranning.basekotlin.model.Message
import com.rikkei.tranning.basekotlin.notifications.NotificationData
import com.rikkei.tranning.basekotlin.notifications.PushNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@HiltViewModel
class ChatRoomVM @Inject constructor() : BaseViewModel() {
    val liveChats: MutableLiveData<List<Message>> by lazy {
        MutableLiveData<List<Message>>(
            ArrayList()
        )
    }
    var topic = ""
    val chats = ArrayList<Message>()
    val currentID: String? = mUser?.uid
    private val currentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    private val currentDate: String =
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    fun sendMessage(msg: String, friends: Friends) {
        if (!TextUtils.isEmpty(msg)) {
            val currentUserRef = "messages/" + mUser?.uid + "/" + friends.Id
            val chatUserRef = "messages/" + friends.Id + "/" + mUser?.uid

            val userMessagePush: DatabaseReference =
                root!!.database.reference.child("messages").child(
                    mUser!!.uid
                ).child(friends.Id).push()
            val pushId = userMessagePush.key
            val messageMap: MutableMap<String, Any> = HashMap()
            messageMap["message"] = msg
            messageMap["seen"] = currentTime
            messageMap["type"] = "text"
            messageMap["time"] = currentDate
            messageMap["from"] = mUser!!.uid
            val messageUserMap: MutableMap<String, Any> = HashMap()
            messageUserMap["$currentUserRef/$pushId"] = messageMap
            messageUserMap["$chatUserRef/$pushId"] = messageMap
            root!!.updateChildren(
                messageUserMap
            ) { error, _ ->
                if (error != null) {
                    throw error.toException()
                }
            }
            topic = "/topics/${friends.Id}"
            mUser?.uid?.let {
                root?.database?.reference?.child("Users")?.child(it)?.get()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val name = task.result.child("Name").value.toString()
                            PushNotification(NotificationData(name, msg), topic)
                                .also { pushNotification ->
                                    sendNotification(pushNotification)
                                }
                        }
                    }
            }

        }
    }

    fun chat(friends: Friends) {
        mUser?.let {
            root?.database?.reference?.child("Chat")?.child(it.uid)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.hasChild(friends.Id)) {
                            val chatAppMap: MutableMap<String, Any> = HashMap()
                            chatAppMap["seen"] = false
                            chatAppMap["timestamp"] = ServerValue.TIMESTAMP
                            val chatUserMap: MutableMap<String, Any> = HashMap()
                            chatUserMap["Chat/" + mUser?.uid + "/" + friends.Id] = chatAppMap
                            chatUserMap["Chat/" + friends.Id + "/" + mUser?.uid] = chatAppMap
                            root!!.database.reference.updateChildren(
                                chatUserMap
                            ) { error, _ ->
                                if (error != null) {
                                    throw error.toException()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        throw error.toException()
                    }
                })
        }
    }

    fun loadMessage(friends: Friends, action: (chats: ArrayList<Message>) -> Unit) {
        val messageRef: DatabaseReference? =
            mUser?.let {
                root?.database?.reference?.child("messages")?.child(it.uid)?.child(friends.Id)
            }
        messageRef?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message: Message? = snapshot.getValue<Message>()
                message?.let { chats.add(it) }
                action(chats)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        liveChats.postValue(chats)
    }

    fun sendImage(uri: Uri?, friends: Friends, action: () -> Unit) {
        val currentUserRef = "messages/" + mUser?.uid + "/" + friends.Id
        val chatUserRef = "messages/" + friends.Id + "/" + mUser?.uid

        val userMessagePush: DatabaseReference? =
            mUser?.let { root?.child("messages")?.child(it.uid)?.child(friends.Id)?.push() }

        val pushId: String = userMessagePush?.key ?: return
        val ref = reference.child("message_image").child("$pushId.jpg")

        val uploadTask = uri?.let { ref.putFile(it) }

        uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val messageMap: MutableMap<String, Any> = HashMap()
                messageMap["message"] = downloadUri.toString()
                messageMap["seen"] = currentTime
                messageMap["type"] = "image"
                messageMap["time"] = currentDate
                messageMap["from"] = mUser!!.uid

                val messageUserMap: MutableMap<String, Any> = HashMap()
                messageUserMap["$currentUserRef/$pushId"] = messageMap
                messageUserMap["$chatUserRef/$pushId"] = messageMap
                action()
                root?.updateChildren(messageUserMap)?.addOnSuccessListener {
                    topic = "/topics/${friends.Id}"
                    mUser!!.email?.let { NotificationData(it, "Đã gửi 1 ảnh") }
                        ?.let { PushNotification(it, topic) }
                        .also {
                            it?.let { it1 -> sendNotification(it1) }
                        }
                }
            }
        }
    }
}

