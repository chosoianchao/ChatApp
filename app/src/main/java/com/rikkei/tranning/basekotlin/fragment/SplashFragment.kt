package com.rikkei.tranning.basekotlin.fragment

import android.view.View
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.SplashBinding
import com.rikkei.tranning.basekotlin.viewmodel.SplashModel

class SplashFragment : BaseFragment<SplashBinding, SplashModel>() {

    companion object {
        val TAG: String = SplashFragment::class.java.name

    }

    override fun initViews() {

    }

    override fun initViewModel(): Class<SplashModel> {
        return SplashModel::class.java
    }

    override fun initViewBinding(view: View): SplashBinding {
        return SplashBinding.bind(view)
    }

    override fun getLayOutId(): Int {
        return R.layout.splash
    }
}