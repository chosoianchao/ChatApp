package com.rikkei.tranning.basekotlin.activity

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.rikkei.tranning.basekotlin.App
import com.rikkei.tranning.basekotlin.LocaleHelper
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseActivity
import com.rikkei.tranning.basekotlin.databinding.ActivityMainBinding
import com.rikkei.tranning.basekotlin.notifications.FirebaseService
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initViews() {
        LocaleHelper().loadLocale(this)
        notification()
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

    private fun notification() {
        if (App.getInstance()?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE) != null) {
            FirebaseService.sharedPref =
                App.getInstance()?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            FirebaseService.token = FirebaseMessaging.getInstance().token.toString()
            val auth: FirebaseAuth? by lazy { FirebaseAuth.getInstance() }
            val mUser: FirebaseUser? by lazy { auth?.currentUser }
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${mUser?.uid}")
        }
    }
}