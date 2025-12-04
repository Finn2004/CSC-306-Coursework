package com.example.coursework1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coursework1.GoalsSelectionFragment
import com.example.coursework1.AccountInfoFragment

class AccountTabsAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(index: Int): Fragment {
        when (index) {
            0 -> return AccountInfoFragment()
            1 -> return GoalsSelectionFragment()
        }
        return AccountInfoFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}