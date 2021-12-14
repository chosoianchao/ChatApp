package com.rikkei.tranning.basekotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.rikkei.tranning.basekotlin.R


abstract class BaseActivity<B : ViewBinding, V : BaseViewModel> : AppCompatActivity(),
    View.OnClickListener {
    protected lateinit var mBinding: B
    protected lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View = LayoutInflater.from(this).inflate(getLayOutId(), null)

        mBinding = initViewBinding(view)
        mViewModel = ViewModelProvider(this)[initViewModel()]

        setContentView(view)
        initViews()
    }

    protected abstract fun initViewBinding(view: View): B

    abstract fun initViewModel(): Class<V>

    protected abstract fun getLayOutId(): Int

    protected abstract fun initViews()

    override fun onClick(v: View?) {
        if (v != null) {
            v.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    androidx.appcompat.R.anim.abc_fade_in
                )
            )
            clickView()
        }
    }

    protected abstract fun clickView()

    fun showFrg(layoutId: Int, tag: String, data: Any?, isBacked: Boolean) {
        try {
            val clazz: Class<*> = Class.forName(tag)
            val frg: BaseFragment<*, *> = clazz.newInstance() as BaseFragment<*, *>

            if (data != null) frg.setData(data)

            val frgMgr: FragmentManager = supportFragmentManager
            val frgTrans: FragmentTransaction = frgMgr.beginTransaction()

            frgTrans.replace(layoutId, frg)

            if (isBacked) {
                frgTrans.addToBackStack(null)
            }

            frgTrans.commit()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showFrg(tag: String) {
        showFrg(R.id.ln_main, tag, null, false)
    }


    fun showFrg(tag: String, isBacked: Boolean) {
        showFrg(R.id.ln_main, tag, null, isBacked)
    }
}






