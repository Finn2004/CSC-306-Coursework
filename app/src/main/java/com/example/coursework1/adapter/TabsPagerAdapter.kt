package com.example.coursework1.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coursework1.GoalsSelectionFragment
import com.example.coursework1.HabitSelectionFragment
import com.example.coursework1.InformationEnteringFragment

class TabsAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(index: Int): Fragment {
        when (index) {
            0 -> return HabitSelectionFragment()
            1 -> return GoalsSelectionFragment()
            2 -> return InformationEnteringFragment()
        }
        return HabitSelectionFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }
}