package com.rikkei.tranning.basekotlin.fragment

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.RequestAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgAllUsersFrBinding
import com.rikkei.tranning.basekotlin.viewmodel.AllUsersVM

class AllUsersFrg : BaseFragment<FrgAllUsersFrBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_all_users_fr

    override val viewModel: AllUsersVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AllUsersFrg.viewModel
        }
    }

    override fun initViews() {
        showListRequest()
        viewModel.liveListRequest.observe(this, {
            val userAdapter: RequestAdapter = viewBinding.rvListUser.adapter as RequestAdapter
            userAdapter.updateUserListItems(it)
        })
    }

    private fun showListRequest() {
        viewBinding.rvListUser.layoutManager = LinearLayoutManager(context)
        viewBinding.rvListUser.hasFixedSize()
        viewBinding.rvListUser.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        if (viewModel.liveListRequest.value != null) {
            val userAdapter = RequestAdapter(
                context, viewModel.liveListRequest.value!!,
                object : OnActionCallBack {
                    override fun callBack(data: Any?, key: String) {
                        if (key == RequestAdapter.ADD_FRIENDS) {
                            val bundle = bundleOf(
                                "DATA_USER" to data
                            )
                            findNavController().navigate(
                                R.id.tabRequestFrg,
                                bundle
                            )
                        }
                    }
                }
            )
            viewBinding.rvListUser.adapter = userAdapter
        }
        viewModel.getData()
    }
}
