package com.autopromaker_pro.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.autopromaker_pro.fragments.InfoFragment
import com.autopromaker_pro.fragments.MainFragment
import com.autopromaker_pro.fragments.SearchKanjiFragment

class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> SearchKanjiFragment()
            else -> InfoFragment()
        }
    }
}