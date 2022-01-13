package com.rikkei.tranning.basekotlin.fragment

import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgRequestBinding
import com.rikkei.tranning.basekotlin.model.User
import com.rikkei.tranning.basekotlin.viewmodel.RequestVM
import java.util.*

class RequestFrg : BaseFragment<FrgRequestBinding>() {

    override val layoutResource: Int
        get() = R.layout.frg_request
    override val viewModel: RequestVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@RequestFrg.viewModel
        }
    }

    override fun initViews() {
        val bundle = arguments
        val user = bundle?.getParcelable<User>("DATA_USER")!!

        viewBinding.tvName.text = user.Name
        viewBinding.tvDob.text = user.Date
        viewBinding.tvDesc.text = user.Description

        if (user.PhotoUrl == "") {
            viewBinding.tvName.text = user.Name
            viewBinding.tvDob.text = user.Date
            viewBinding.tvDesc.text = user.Description
            viewBinding.ivAvatar.setImageResource(R.drawable.ic_user)
        } else {
            viewBinding.tvName.text = user.Name
            viewBinding.tvDob.text = user.Date
            viewBinding.tvDesc.text = user.Description
            context?.let { Glide.with(it).load(user.PhotoUrl).into(viewBinding.ivAvatar) }
        }
        viewModel.featureFriends(user, ::actionAccept, ::cancelAction, ::unFriendAction)
        viewBinding.btAdd.setOnClickListener {
            viewModel.addFriends(user, ::sendAction, ::cancelAction, ::unFriendAction)
        }
        viewBinding.btDecline.setOnClickListener {
            viewModel.declineFriends(user, ::declineAction)
        }
    }

    private fun declineAction() {
        viewBinding.btAdd.text = getString(R.string.send_friend_request)
        viewBinding.btDecline.visibility = View.INVISIBLE
    }

    private fun actionAccept() {
        viewBinding.btAdd.text = getString(R.string.accept_friends_request)
        viewBinding.btDecline.visibility = View.VISIBLE
    }

    private fun unFriendAction() {
        viewBinding.btAdd.text = getString(R.string.text_unfriend)
        viewBinding.btDecline.visibility = View.INVISIBLE
    }

    private fun cancelAction() {
        viewBinding.btAdd.text = getString(R.string.send_friend_request)
    }

    private fun sendAction() {
        viewBinding.btAdd.text = getString(R.string.cancel_friend_request)
    }
}

