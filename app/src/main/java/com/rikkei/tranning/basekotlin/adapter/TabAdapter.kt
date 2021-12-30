package com.rikkei.tranning.basekotlin.adapter


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg.TabAllFrg
import com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg.TabFriendsFrg
import com.rikkei.tranning.basekotlin.fragment.tabfriendsfrg.TabRequestFrg

class TabAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TabFriendsFrg()
            1 -> return TabAllFrg()
            2 -> return TabRequestFrg()
        }
        return TabFriendsFrg()
    }

    override fun getItemCount(): Int {
        return 3
    }
}