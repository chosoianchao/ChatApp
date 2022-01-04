package com.rikkei.tranning.basekotlin.viewmodel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rikkei.tranning.basekotlin.base.BaseViewModel
import com.rikkei.tranning.basekotlin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RequestVM @Inject constructor() : BaseViewModel() {

    var currentState: String? = NOT_FRIENDS

    fun addFriends(
        data: Any?,
        sendAction: () -> Unit,
        cancelAction: () -> Unit,
        unfriendAction: () -> Unit
    ) {
        val user: User = data as User
        if (currentState == NOT_FRIENDS) {
            mUser?.uid?.let { send ->
                user.Id.let { receive ->
                    root?.database?.reference
                        ?.child(FRIENDS_REQ)
                        ?.child(send)
                        ?.child(receive)?.child(REQUEST_TYPE)?.setValue(SENT)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                root!!.database.reference
                                    .child(FRIENDS_REQ)
                                    .child(receive)
                                    .child(send)
                                    .child(REQUEST_TYPE)
                                    .setValue(RECEIVED).addOnSuccessListener {
                                        currentState = REQUEST_SENT
                                        sendAction()
                                    }
                            }
                        }
                }
            }
        }
        if (currentState == REQUEST_SENT) {
            mUser?.uid?.let {
                root?.database?.reference
                    ?.child(FRIENDS_REQ)
                    ?.child(it)
                    ?.child(user.Id)
                    ?.removeValue()
                    ?.addOnSuccessListener {
                        root!!.database.reference.child(FRIENDS_REQ)
                            .child(user.Id)
                            .child(mUser?.uid!!)
                            .removeValue()
                            .addOnSuccessListener {
                                currentState = NOT_FRIENDS
                                cancelAction()
                            }
                    }
            }
        }
        if (currentState == REQUEST_RECEIVED) {
            val currentDate = DateFormat.getDateTimeInstance().format(Date())
            mUser?.uid?.let {
                root?.database?.reference?.child(USERS)?.child(it)?.get()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val date = task.result.child(DATE).value
                            val name = task.result.child(NAME).value
                            val photo = task.result.child(PHOTO).value
                            val id = task.result.child(ID).value
                            val desc = task.result.child(DESC).value
                            val currentUser =
                                root?.database?.reference!!.child(FRIENDS).child(user.Id).child(
                                    mUser!!.uid
                                )
                            currentUser.child(NAME).setValue(name)
                            currentUser.child(PHOTO).setValue(photo)
                            currentUser.child(ID).setValue(id)
                            currentUser.child(DATE).setValue(date)
                            currentUser.child(DESC).setValue(desc)
                        }
                    }
            }
            mUser?.uid?.let {
                root?.database?.reference?.child(FRIENDS)?.child(it)?.child(user.Id)?.child(TIME)
                    ?.setValue(currentDate)
            }?.addOnSuccessListener {
                val friendsUser =
                    root?.database?.reference?.child(FRIENDS)?.child(mUser!!.uid)?.child(user.Id)
                friendsUser?.child(NAME)?.setValue(user.Name)
                friendsUser?.child(PHOTO)?.setValue(user.PhotoUrl)
                friendsUser?.child(ID)?.setValue(user.Id)
                friendsUser?.child(DATE)?.setValue(user.Date)
                friendsUser?.child(DESC)?.setValue(user.Description)
                root?.database?.reference?.child(FRIENDS)?.child(user.Id)
                    ?.child(mUser?.uid!!)?.child(TIME)
                    ?.setValue(currentDate)
                    ?.addOnSuccessListener {
                        mUser?.uid?.let {
                            root?.database?.reference
                                ?.child(FRIENDS_REQ)
                                ?.child(it)
                                ?.child(user.Id)
                                ?.removeValue()
                                ?.addOnSuccessListener {
                                    root!!.database.reference.child(FRIENDS_REQ)
                                        .child(user.Id)
                                        .child(mUser?.uid!!)
                                        .removeValue()
                                        .addOnSuccessListener {
                                            currentState = FRIENDS
                                            unfriendAction()
                                        }
                                }
                        }
                    }
            }
        }
        if (currentState == FRIENDS) {
            mUser?.uid?.let {
                root?.database?.reference
                    ?.child(FRIENDS)
                    ?.child(it)
                    ?.removeValue()
                    ?.addOnSuccessListener {
                        root!!.database.reference.child(FRIENDS)
                            .child(user.Id)
                            .removeValue()
                            .addOnSuccessListener {
                                currentState = NOT_FRIENDS
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
            root?.database?.reference?.child(FRIENDS_REQ)?.child(it)
                ?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(user.Id)) {
                            val requestType: String =
                                snapshot.child(user.Id).child(REQUEST_TYPE).value.toString()
                            if (requestType == RECEIVED) {
                                currentState = REQUEST_RECEIVED
                                actionAccept()
                            } else if (requestType == SENT) {
                                currentState = REQUEST_SENT
                                actionCancel()
                            }
                        } else {
                            root!!.database.reference.child(FRIENDS).child(mUser!!.uid)
                                .addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(user.Id)) {
                                            currentState = FRIENDS
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
                ?.child(FRIENDS_REQ)
                ?.child(it)
                ?.child(user.Id)
                ?.removeValue()
                ?.addOnSuccessListener {
                    root!!.database.reference.child(FRIENDS_REQ)
                        .child(user.Id)
                        .child(mUser?.uid!!)
                        .removeValue()
                        .addOnSuccessListener {
                            currentState = NOT_FRIENDS
                            declineAction()
                        }
                }
        }
    }

    companion object {
        internal const val NOT_FRIENDS: String = "not_friends"
        internal const val FRIENDS: String = "Friends"
        private const val PHOTO: String = "PhotoUrl"
        private const val TIME: String = "Time"
        private const val ID: String = "Id"
        private const val USERS: String = "Users"
        private const val NAME: String = "Name"
        private const val DATE: String = "Date"
        private const val DESC: String = "Description"
        internal const val REQUEST_SENT: String = "req_sent"
        private const val REQUEST_TYPE: String = "request_type"
        private const val REQUEST_RECEIVED: String = "request_received"
        private const val FRIENDS_REQ: String = "Friends_req"
        private const val RECEIVED: String = "received"
        private const val SENT: String = "sent"
    }
}