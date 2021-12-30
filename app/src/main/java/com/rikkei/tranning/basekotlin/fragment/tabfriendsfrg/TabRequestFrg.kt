package com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.adapter.ActionCallBack
import com.rikkei.tranning.basekotlin.adapter.RequestAdapter
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgTabRequestBinding
import com.rikkei.tranning.basekotlin.viewmodel.tabfriendsvm.TabRequestVM
import java.util.*

class TabRequestFrg : BaseFragment<FrgTabRequestBinding>() {

    override val layoutResource: Int
        get() = R.layout.frg_tab_request
    override val viewModel: TabRequestVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TabRequestFrg.viewModel
        }
    }

    override fun initViews() {
        showListRequest()
        viewModel.liveUser.observe(this, {
            val request: RequestAdapter = viewBinding.rvListRequest.adapter as RequestAdapter
            request.updateEmployeeListItems(it)
        })
    }

    private fun showListRequest() {
        viewModel.getData()

        viewBinding.rvListRequest.layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        viewBinding.rvListRequest.addItemDecoration(decoration)

        if (viewModel.liveUser.value != null) {
            val userAdapter = RequestAdapter(context, viewModel.liveUser.value!!,
                object : ActionCallBack {
                    override fun callBack(data: Any?, key: String) {
                        if (key == "aaa") {
                            Log.d("Thang", "callBack() called with: data = $data, key = $key")
                            viewModel.addFriends()
                        }
                    }
                })
            viewBinding.rvListRequest.adapter = userAdapter
        }
    }
}