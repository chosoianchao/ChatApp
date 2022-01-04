package com.rikkei.tranning.basekotlin.activity

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseActivity
import com.rikkei.tranning.basekotlin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
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
            when (destination.id) {
                R.id.mainChatFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.VISIBLE
                }
                R.id.loginFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.GONE
                }
                R.id.modifyInformationFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.GONE
                }
                R.id.personalPageFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.VISIBLE
                }
                R.id.tabRequestFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.GONE
                }
                R.id.friendsFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.VISIBLE
                }
                R.id.createMessageWithFriendsFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.GONE
                }
                R.id.chatRoomFrg -> {
                    mBinding?.groupBottomNav?.visibility = View.GONE
                }
            }
        }
    }
}