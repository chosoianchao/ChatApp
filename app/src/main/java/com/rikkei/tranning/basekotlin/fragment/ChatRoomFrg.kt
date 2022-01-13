package com.rikkei.tranning.basekotlin.fragment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.ChatAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgChatRoomBinding
import com.rikkei.tranning.basekotlin.model.Friends
import com.rikkei.tranning.basekotlin.model.Message
import com.rikkei.tranning.basekotlin.showToastLong
import com.rikkei.tranning.basekotlin.viewmodel.ChatRoomVM
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.EmojiTextView
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class ChatRoomFrg : BaseFragment<FrgChatRoomBinding>() {
    private var getImageFromCamera: ActivityResultLauncher<Intent>? = null
    private var requestPermission: ActivityResultLauncher<String>? = null
    private val constrain: ConstraintLayout? = null
    private var friends: Friends? = null

    override val layoutResource: Int
        get() = R.layout.frg_chat_room

    override val viewModel: ChatRoomVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatRoomFrg.viewModel
        }
    }

    private val getImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.sendImage(uri, friends!!, ::action)
        }

    override fun initViews() {
        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (!it) {
                    context?.showToastLong(getString(R.string.permission_denied))
                    viewBinding.ivAddPhoto.isClickable = false
                } else {
                    viewBinding.ivAddPhoto.isClickable = true
                }
            }
        requestPermission?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val popup: EmojiPopup? =
            EmojiPopup.Builder.fromRootView(viewBinding.rootView).build(viewBinding.edtMessage)
        viewBinding.ivEmoji.setOnClickListener {
            popup?.toggle()
        }
        viewBinding.ivAddPhoto.setOnClickListener {
            selectImage()
        }
        val bundle = arguments
        friends = bundle?.getParcelable("Friends")!!
        viewModel.chat(friends!!)
        if (friends?.PhotoUrl == "") {
            viewBinding.tvName.text = friends?.Name
            viewBinding.ivAvatar.setImageResource(R.drawable.ic_user)
        } else {
            viewBinding.tvName.text = friends?.Name
            context?.let { Glide.with(it).load(friends?.PhotoUrl).into(viewBinding.ivAvatar) }
        }
        viewBinding.ivBack.setOnClickListener {
            val action = ChatRoomFrgDirections.actionChatRoomFrgToMainChatFrg()
            findNavController().navigate(action)
        }

        showListChat(friends!!)
        viewModel.liveChats.observe(this, {
            val chat: ChatAdapter = viewBinding.rvChatRoom.adapter as ChatAdapter
            chat.setData(it)
        })

        viewBinding.ivSend.setOnClickListener {
            val emojiTextView: EmojiTextView =
                LayoutInflater.from(context)
                    .inflate(R.layout.emoji_text_view, constrain, false) as EmojiTextView
            emojiTextView.text = viewBinding.edtMessage.text.toString()
            constrain?.addView(emojiTextView)
            val msg = viewBinding.edtMessage.text.toString()
            viewModel.sendMessage(msg, friends!!)
            viewBinding.edtMessage.text.clear()
        }
        viewBinding.swipe.setOnRefreshListener {
            refreshFragment()
            viewBinding.swipe.isRefreshing = false
        }
        getImageFromCamera(friends!!)
    }

    private fun refreshFragment() {
        val bundle = bundleOf(
            "Friends" to friends
        )
        val id = findNavController().currentDestination?.id
        findNavController().popBackStack(id!!, true)
        findNavController().navigate(id, bundle)
    }

    private fun action() {
        viewBinding.edtMessage.setText("")
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.text_take_photo),
            getString(R.string.text_choose_image),
            getString(R.string.cancel)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.text_add_photo))
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.text_take_photo) -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    activity?.packageManager.let {
                        if (it?.resolveActivity(intent, 0) != null) {
                            getImageFromCamera?.launch(intent)
                        }
                    }
                }
                options[item] == getString(R.string.text_choose_image) -> {
                    getImageFromGallery.launch("image/*")
                }
                options[item] == getString(R.string.cancel) -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun getImageFromCamera(friends: Friends) {
        getImageFromCamera =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                    val bundle: Bundle? = it.data!!.extras
                    val bitmap: Bitmap? = bundle?.get("data") as Bitmap?

                    val file = File(context?.cacheDir, "CUSTOM NAME")

                    file.delete()
                    file.createNewFile()
                    val fileOutputStream = file.outputStream()
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val bytearray = byteArrayOutputStream.toByteArray()
                    fileOutputStream.write(bytearray)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    byteArrayOutputStream.close()

                    val uri = file.toUri()
                    viewModel.sendImage(uri, friends, ::action)
                }
            }
    }

    private fun showListChat(friends: Friends) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        viewBinding.rvChatRoom.layoutManager = linearLayoutManager
        viewBinding.rvChatRoom.hasFixedSize()
        if (viewModel.liveChats.value != null) {
            val chat = ChatAdapter(
                context, viewModel.currentID, friends.PhotoUrl, viewModel.liveChats.value!!
            )
            viewBinding.rvChatRoom.adapter = chat
        }
        viewModel.loadMessage(friends, ::setView)
    }

    private fun setView(messageList: ArrayList<Message>) {
        viewBinding.rvChatRoom.scrollToPosition(messageList.size - 1)
    }
}