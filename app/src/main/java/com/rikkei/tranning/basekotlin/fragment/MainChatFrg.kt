package com.rikkei.tranning.basekotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgMainChatBinding
import com.rikkei.tranning.basekotlin.model.Conv
import com.rikkei.tranning.basekotlin.model.Friends
import com.rikkei.tranning.basekotlin.model.Message
import com.rikkei.tranning.basekotlin.viewmodel.MainChatVM
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainChatFrg : BaseFragment<FrgMainChatBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_main_chat

    override val viewModel: MainChatVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@MainChatFrg.viewModel
        }
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        viewBinding.ivCreateNewMessage.setOnClickListener {
            val action = MainChatFrgDirections.actionMainChatFrgToCreateMessageWithFriendsFrg()
            findNavController().navigate(action)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        viewBinding.rvListChat.hasFixedSize()
        viewBinding.rvListChat.layoutManager = linearLayoutManager
    }

    override fun onStart() {
        super.onStart()
        showListConversation()
    }

    private fun showListConversation() {
        val optionsConversation = FirebaseRecyclerOptions.Builder<Conv>()
            .setQuery(viewModel.conversationQuery!!, Conv::class.java).setLifecycleOwner(this)
            .build()
        val adapterConversation =
            object : FirebaseRecyclerAdapter<Conv, ConvViewHolder>(optionsConversation) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvViewHolder {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(context)
                    return ConvViewHolder(
                        layoutInflater.inflate(R.layout.item_list_chat, parent, false), context
                    )
                }

                override fun onBindViewHolder(holder: ConvViewHolder, i: Int, model: Conv) {
                    val listUserId: String? = getRef(i).key
                    getMessage(listUserId, holder)
                    getInfo(listUserId, holder)
                }
            }
        if (viewBinding.edtSearchMessage.text.toString() == "") {
            viewBinding.rvListChat.adapter = adapterConversation
            adapterConversation.onDataChanged()
        }
        viewBinding.edtSearchMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val msg = p0.toString()
                searchMsg(msg)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun searchMsg(msg: String) {
        val optionsSearch = FirebaseRecyclerOptions.Builder<Friends>()
            .setQuery(
                viewModel.mFriendsDatabase?.orderByChild("Name")?.startAt(msg)
                    ?.endAt(msg + "\uf8ff")!!, Friends::class.java
            ).setLifecycleOwner(this)
            .build()
        val adapterSearch =
            object : FirebaseRecyclerAdapter<Friends, ConvViewHolder>(optionsSearch) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvViewHolder {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(context)
                    return ConvViewHolder(
                        layoutInflater.inflate(R.layout.item_list_chat, parent, false), context
                    )
                }

                override fun onBindViewHolder(holder: ConvViewHolder, i: Int, model: Friends) {
                    val listUserId: String? = getRef(i).key
                    getMessage(listUserId, holder)
                    getInfo(listUserId, holder)
                }
            }
        viewBinding.rvListChat.adapter = adapterSearch
        adapterSearch.onDataChanged()
    }

    private fun getMessage(listUserId: String?, holder: ConvViewHolder) {
        val lastMessageQuery: Query =
            listUserId?.let { viewModel.mMessageDatabase?.child(it) }!!.limitToLast(1)
        lastMessageQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message: Message? = snapshot.getValue<Message>()
                message?.message?.let { holder.setMessage(it) }
                holder.setTime(message?.seen, message?.time)
                if (message?.type == "image") {
                    holder.setMessage(getString(R.string.text_sent_photo))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    private fun getInfo(listUserId: String?, holder: ConvViewHolder) {
        listUserId?.let {
            viewModel.mFriendsDatabase?.child(it)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val friends: Friends? = snapshot.getValue<Friends>()
                        val userName: String? = friends?.Name
                        val image: String? = friends?.PhotoUrl
                        userName?.let { s -> holder.setName(s) }
                        image?.let { context?.let { context -> holder.setImage(it, context) } }
                        holder.view.setOnClickListener {
                            val bundle = bundleOf(
                                "Friends" to friends
                            )
                            findNavController().navigate(R.id.chatRoomFrg, bundle)
                        }
                        holder.view.setOnLongClickListener {
                            val dialog: AlertDialog = AlertDialog.Builder(context).create()
                            dialog.setTitle(getString(R.string.text_notify))
                            dialog.setCancelable(false)
                            dialog.setCanceledOnTouchOutside(false)
                            dialog.setMessage(getString(R.string.text_delete_message))
                            dialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE,
                                getString(R.string.text_yes)
                            ) { _, _ ->
                                viewModel.remove(friends)
                            }
                            dialog.setButton(
                                AlertDialog.BUTTON_POSITIVE,
                                getString(R.string.text_no)
                            ) { _, _ ->
                                dialog.dismiss()
                            }
                            dialog.show()
                            true
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        throw error.toException()
                    }
                })
        }
    }

    companion object {
        class ConvViewHolder(val view: View, val context: Context?) : RecyclerView.ViewHolder(
            view
        ) {
            fun setMessage(message: String) {
                val userStatusView: TextView = view.findViewById(R.id.tv_message)
                userStatusView.text = message
            }

            fun setName(name: String) {
                val userNameView: TextView = view.findViewById(R.id.tv_name)
                userNameView.text = name
            }

            fun setTime(time: String?, date: String?) {
                val currentDate: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, -1)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val yesterday: String = dateFormat.format(calendar.time)
                val timeChat: TextView = view.findViewById(R.id.tv_time)
                when (date) {
                    currentDate -> {
                        timeChat.text = time
                    }
                    yesterday -> {
                        timeChat.text = context!!.getString(R.string.text_yesterday)
                    }
                    else -> {
                        timeChat.text = date
                    }
                }
            }

            fun setImage(image: String, context: Context) {
                val userImageView: CircleImageView = view.findViewById(R.id.iv_avatar)
                if (image == "") {
                    userImageView.setImageResource(R.drawable.ic_user)
                } else {
                    Glide.with(context).load(image).into(userImageView)
                }
            }
        }
    }
}

