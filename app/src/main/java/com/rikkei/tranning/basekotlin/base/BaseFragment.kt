package com.rikkei.tranning.basekotlin.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, V : BaseViewModel> : Fragment(), View.OnClickListener {
    protected lateinit var mBinding: B
    protected lateinit var mViewModel: V
    protected lateinit var data: Any


    @JvmName("setData1")
    fun setData(data: Any) {
        this.data = data
    }

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

    override fun onClick(v: View?) {
        v?.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                androidx.appcompat.R.anim.abc_fade_in
            )
        )
        clickView()
    }

    abstract fun clickView()
}