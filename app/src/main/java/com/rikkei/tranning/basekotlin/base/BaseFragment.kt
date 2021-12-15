package com.rikkei.tranning.basekotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, V : BaseViewModel> : Fragment() {
    protected var mBinding: B? = null
    protected var mViewModel: V? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(getLayOutId(), container, false)
        mBinding = initViewBinding(view)
        mViewModel = ViewModelProvider(this)[initViewModel()]
        initViews()
        return view
    }

    protected abstract fun initViews()

    protected abstract fun initViewModel(): Class<V>

    protected abstract fun initViewBinding(view: View): B

    protected abstract fun getLayOutId(): Int
}