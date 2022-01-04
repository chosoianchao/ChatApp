package com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.tranning.basekotlin.OnActionCallBack
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.RequestAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgTabAllFrBinding
import com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm.TabAllVM

class TabAllFrg : BaseFragment<FrgTabAllFrBinding>() {
    override val layoutResource: Int
        get() = R.layout.frg_tab_all_fr

    override val viewModel: TabAllVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TabAllFrg.viewModel
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
                                DATA_USER to data
                            )
                            findNavController().navigate(
                                R.id.action_friendsFrg_to_tabRequestFrg,
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

    companion object {
        const val DATA_USER: String = "DATA_USER"
    }
}
