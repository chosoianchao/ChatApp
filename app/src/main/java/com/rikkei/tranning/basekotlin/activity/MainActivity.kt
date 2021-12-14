package com.rikkei.tranning.basekotlin.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.fragment.SplashFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
showFragment(SplashFragment(), SplashFragment.TAG, false)

    }

    private fun showFragment(fragment: Fragment, tag: String, isBacked: Boolean) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            if(isBacked) addToBackStack(tag)
        }
    }


}