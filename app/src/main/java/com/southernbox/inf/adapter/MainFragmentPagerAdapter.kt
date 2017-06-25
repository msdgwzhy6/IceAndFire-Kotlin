package com.southernbox.inf.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.southernbox.inf.entity.TabDTO
import com.southernbox.inf.fragment.MainFragment

/**
 * Created by SouthernBox on 2017/6/25 0025.
 * 首页ViewPager适配器
 */

class MainFragmentPagerAdapter(fm: FragmentManager, fragments: List<MainFragment>, tabList: List<TabDTO>)
    : FragmentStatePagerAdapter(fm) {

    var fragments: List<MainFragment>? = null
    var tabList: List<TabDTO>? = null

    init {
        this.fragments = fragments
        this.tabList = tabList
    }

    override fun getItem(position: Int): Fragment = fragments!![position]

    override fun getCount(): Int = if (fragments != null) fragments!!.size else 0

    override fun getPageTitle(position: Int): CharSequence =
            if (tabList != null) tabList!![position].title as CharSequence else ""
}