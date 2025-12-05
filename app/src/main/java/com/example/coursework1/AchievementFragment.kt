package com.example.coursework1

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework1.adapter.WidgetAchievementAdapter
import com.example.coursework1.adapter.WidgetAchievementInfo

class AchievementFragment : Fragment() {
    private lateinit var adapter: WidgetAchievementAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_achievements, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())
        val userID = database.getUserIdByUsername(sharedPreferences?.getString("User", null))

        val recyclerView = view.findViewById<RecyclerView>(R.id.achievements_list)
        adapter = WidgetAchievementAdapter {}
        populate(adapter)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    fun populate(adapter: WidgetAchievementAdapter) : WidgetAchievementAdapter {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())

        val userID = database.getUserIdByUsername(sharedPreferences?.getString("User", null))


        val achievements = database.getUserAchievements(userID)
        val achievementsNum = achievements.size

        for (i in 0 until achievementsNum) {

            val text = achievements[i][0]
            val progress = achievements[i][1].toInt()
            val max = achievements[i][2].toInt()
            val metric = achievements[i][3]
            val achievementId = achievements[i][4].toInt()

            val newWidget = WidgetAchievementInfo(
                achievementId = achievementId,
                widgetText = text,
                widgetProgress = progress,
                widgetMax = max,
                widgetMetric = metric
            )

            adapter.addWidget(newWidget)

        }
        return adapter
    }
}