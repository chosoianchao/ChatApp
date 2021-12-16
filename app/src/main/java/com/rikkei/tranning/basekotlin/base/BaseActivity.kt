package com.rikkei.tranning.basekotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    protected var mBinding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val view: View = LayoutInflater.from(this).inflate(getLayOutId(), null)
        mBinding = initViewBinding(view)

        setContentView(view)
        initViews()
    }

    protected abstract fun initViewBinding(view: View): B

    protected abstract fun getLayOutId(): Int

    protected abstract fun initViews()

}






