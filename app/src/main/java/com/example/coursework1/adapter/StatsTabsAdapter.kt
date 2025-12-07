package com.example.coursework1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coursework1.AccountInfoFragment
import com.example.coursework1.BarChartHorizontalFragment
import com.example.coursework1.BarChartFragment
import com.example.coursework1.PieChartFragment

class StatsTabsAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(index: Int): Fragment {
        when (index) {
            0 -> return BarChartFragment()
            1 -> return BarChartHorizontalFragment()
            2 -> return PieChartFragment()
        }
        return AccountInfoFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }

    fun getStatName(index: Int) : String {
        when (index) {
            0 -> return "Completed Challenges"
            1 -> return "Completed Daily Habits"
            2 -> return "Habit Tasks Breakdown"
        }

        return "Competed Challenges"
    }
}