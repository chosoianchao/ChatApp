package com.rikkei.tranning.basekotlin.activity

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseActivity
import com.rikkei.tranning.basekotlin.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
    }
    private val navController by lazy { navHostFragment.navController }

    override fun initViewBinding(view: View): ActivityMainBinding {
        return ActivityMainBinding.bind(view)
    }

    override fun getLayOutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        mBinding?.bottomNavigation?.apply {
            setupWithNavController(navController)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splashFragment) {
                mBinding?.groupBottomNav?.visibility = View.VISIBLE
            }
        }
    }
}