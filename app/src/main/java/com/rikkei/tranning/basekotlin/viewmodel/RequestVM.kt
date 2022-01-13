package com.rikkei.tranning.basekotlin.viewmodel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rikkei.tranning.basekotlin.App
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import com.rikkei.tranning.basekotlin.notifications.NotificationData
import com.rikkei.tranning.basekotlin.notifications.PushNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RequestVM @Inject constructor() : BaseViewModel() {

    var currentState: String? = "not_friends"
    var topic = ""

    fun addFriends(
        data: Any?,
        sendAction: () -> Unit,
        cancelAction: () -> Unit,
        unfriendAction: () -> Unit
    ) {
        val user: User = data as User
        if (currentState == "not_friends") {
            mUser?.uid?.let { send ->
                user.Id.let { receive ->
                    root?.database?.reference
                        ?.child("Friends_req")
                        ?.child(send)
                        ?.child(receive)?.child("request_type")?.setValue("sent")
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                root!!.database.reference
                                    .child("Friends_req")
                                    .child(receive)
                                    .child(send)
                                    .child("request_type")
                                    .setValue("received").addOnSuccessListener {
                                        currentState = "req_sent"
                                        sendAction()
                                        topic = "/topics/${user.Id}"
                                        mUser?.uid?.let { s ->
                                            root?.database?.reference?.child("Users")?.child(s)
                                                ?.get()
                                                ?.addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        val name =
                                                            task.result.child("Name").value.toString()
                                                        PushNotification(
                                                            NotificationData(
                                                                name,
                                                                App.getInstance()!!
                                                                    .getString(R.string.text_sent_request)
                                                            ), topic
                                                        )
                                                            .also { pushNotification ->
                                                                sendNotification(pushNotification)
                                                            }
                                                    }
                                                }
                                        }
                                    }
                            }
                        }
                }
            }
        }
        if (currentState == "req_sent") {
            mUser?.uid?.let {
                root?.database?.reference
                    ?.child("Friends_req")
                    ?.child(it)
                    ?.child(user.Id)
                    ?.removeValue()
                    ?.addOnSuccessListener {
                        root!!.database.reference.child("Friends_req")
                            .child(user.Id)
                            .child(mUser?.uid!!)
                            .removeValue()
                            .addOnSuccessListener {
                                currentState = "not_friends"
                                cancelAction()
                            }
                    }
            }
        }
        if (currentState == "request_received") {
            val currentDate = DateFormat.getDateTimeInstance().format(Date())
            mUser?.uid?.let {
                root?.database?.reference?.child("Users")?.child(it)?.get()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val date = task.result.child("Date").value
                            val name = task.result.child("Name").value
                            val photo = task.result.child("PhotoUrl").value
                            val id = task.result.child("Id").value
                            val desc = task.result.child("Description").value
                            val currentUser =
                                root?.database?.reference!!.child("Friends").child(user.Id).child(
                                    mUser!!.uid
                                )
                            currentUser.child("Name").setValue(name)
                            currentUser.child("PhotoUrl").setValue(photo)
                            currentUser.child("Id").setValue(id)
                            currentUser.child("Date").setValue(date)
                            currentUser.child("Description").setValue(desc)
                        }
                    }
            }
            mUser?.uid?.let {
                root?.database?.reference?.child("Friends")?.child(it)?.child(user.Id)
                    ?.child("Time")
                    ?.setValue(currentDate)
            }?.addOnSuccessListener {
                val friendsUser =
                    root?.database?.reference?.child("Friends")?.child(mUser!!.uid)?.child(user.Id)
                friendsUser?.child("Name")?.setValue(user.Name)
                friendsUser?.child("PhotoUrl")?.setValue(user.PhotoUrl)
                friendsUser?.child("Id")?.setValue(user.Id)
                friendsUser?.child("Date")?.setValue(user.Date)
                friendsUser?.child("Description")?.setValue(user.Description)
                root?.database?.reference?.child("Friends")?.child(user.Id)
                    ?.child(mUser?.uid!!)?.child("Time")
                    ?.setValue(currentDate)
                    ?.addOnSuccessListener {
                        mUser?.uid?.let {
                            root?.database?.reference
                                ?.child("Friends_req")
                                ?.child(it)
                                ?.child(user.Id)
                                ?.removeValue()
                                ?.addOnSuccessListener {
                                    root!!.database.reference.child("Friends_req")
                                        .child(user.Id)
                                        .child(mUser?.uid!!)
                                        .removeValue()
                                        .addOnSuccessListener {
                                            currentState = "Friends"
                                            unfriendAction()
                                            topic = "/topics/${user.Id}"
                                            mUser?.uid?.let { s ->
                                                root?.database?.reference?.child("Users")?.child(s)
                                                    ?.get()
                                                    ?.addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            val name =
                                                                task.result.child("Name").value.toString()
                                                            PushNotification(
                                                                NotificationData(
                                                                    name,
                                                                    App.getInstance()!!
                                                                        .getString(R.string.text_accept)
                                                                ), topic
                                                            )
                                                                .also { pushNotification ->
                                                                    sendNotification(
                                                                        pushNotification
                                                                    )
                                                                }
                                                        }
                                                    }
                                            }
                                        }
                                }
                        }
                    }
            }
        }
        if (currentState == "Friends") {
            mUser?.uid?.let {
                root?.database?.reference
                    ?.child("Friends")
                    ?.child(it)
                    ?.removeValue()
                    ?.addOnSuccessListener {
                        root!!.database.reference.child("Friends")
                            .child(user.Id)
                            .removeValue()
                            .addOnSuccessListener {
                                currentState = "not_friends"
                                cancelAction()
                            }
                    }
            }
        }
    }

    fun featureFriends(
        user: User,
        actionAccept: () -> Unit,
        actionCancel: () -> Unit,
        unfriendAction: () -> Unit
    ) {
        mUser?.uid?.let {
            root?.database?.reference?.child("Friends_req")?.child(it)
                ?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(user.Id)) {
                            val requestType: String =
                                snapshot.child(user.Id).child("request_type").value.toString()
                            if (requestType == "received") {
                                currentState = "request_received"
                                actionAccept()
                            } else if (requestType == "sent") {
                                currentState = "req_sent"
                                actionCancel()
                            }
                        } else {
                            root!!.database.reference.child("Friends").child(mUser!!.uid)
                                .addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(user.Id)) {
                                            currentState = "Friends"
                                            unfriendAction()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        throw error.toException()
                                    }
                                })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        throw error.toException()
                    }
                })
        }
    }

    fun declineFriends(user: User, declineAction: () -> Unit) {
        mUser?.uid?.let {
            root?.database?.reference
                ?.child("Friends_req")
                ?.child(it)
                ?.child(user.Id)
                ?.removeValue()
                ?.addOnSuccessListener {
                    root!!.database.reference.child("Friends_req")
                        .child(user.Id)
                        .child(mUser?.uid!!)
                        .removeValue()
                        .addOnSuccessListener {
                            currentState = "not_friends"
                            declineAction()
                        }
                }
        }
    }
}