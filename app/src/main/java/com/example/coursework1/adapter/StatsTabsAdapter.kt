package com.example.coursework1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coursework1.AccountInfoFragment
import com.example.coursework1.AchievementFragment
import com.example.coursework1.BarChartFragment

class StatsTabsAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(index: Int): Fragment {
        when (index) {
            0 -> return BarChartFragment()
            1 -> return AchievementFragment()
        }
        return AccountInfoFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }

    fun getStatName(index: Int) : String {
        when (index) {
            0 -> return "Completed Challenges"
            1 -> return "placeholder"
        }

        return "Competed Challenges"
    }
}