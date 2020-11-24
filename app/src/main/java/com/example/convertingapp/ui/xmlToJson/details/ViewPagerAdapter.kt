package com.example.convertingapp.ui.xmlToJson.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: Fragment) : FragmentStateAdapter(fm) {

    private var fragmentList = arrayListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        this.fragmentList.add(fragment)
    }
}