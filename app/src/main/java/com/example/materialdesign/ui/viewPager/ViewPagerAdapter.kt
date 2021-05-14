package com.example.materialdesign.ui.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

private const val TODAY_POD = 0
private const val YESTERDAY_POD = 1
private const val DAY_BEFORE_YESTERDAY_POD = 2

class ViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments =
        arrayOf(TodayFragment(), YesterdayFragment(), DayBeforeYesterdayFragment())

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[TODAY_POD]
            1 -> fragments[YESTERDAY_POD]
            2 -> fragments[DAY_BEFORE_YESTERDAY_POD]
            else -> fragments[TODAY_POD]
        }
    }
}