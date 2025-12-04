package com.example.coursework1

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework1.adapter.AccountTabsAdapter
import com.example.coursework1.adapter.WidgetGoalAdapter
import com.example.coursework1.adapter.WidgetGoalInfo
import com.google.android.material.progressindicator.LinearProgressIndicator

class AccountInfoFragment : Fragment() {

    private lateinit var adapter: WidgetGoalAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_account_info, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())

        val userID = database.getUserIdByUsername(sharedPreferences?.getString("User", null))
        val levelInfo = database.getLevelUpInfo(userID)

        val level = view.findViewById<TextView>(R.id.level)
        val levelText = "Level " + levelInfo[0]
        level.text = levelText

        val progressBar = view.findViewById<LinearProgressIndicator>(R.id.progress_bar)
        progressBar.progress = levelInfo[1].toInt()
        progressBar.max = levelInfo[2].toInt()

        val progress = view.findViewById<TextView>(R.id.progress_text)
        val progressText = levelInfo[1] + " / " + levelInfo[2] + " XP"
        progress.text = progressText

        val recyclerView = view.findViewById<RecyclerView>(R.id.completed_goals_list)
        adapter = WidgetGoalAdapter {}
        populate(adapter)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    fun populate(adapter: WidgetGoalAdapter) : WidgetGoalAdapter {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())

        val userID = database.getUserIdByUsername(sharedPreferences?.getString("User", null))


        val userGoals = database.getUserGoals(userID)
        val userGoalsNum = userGoals.size

        for (i in 0 until userGoalsNum) {
            if (database.getGoalCompletedStatus(userGoals[i][4].toInt())) {

                val text = userGoals[i][0]
                val progress = userGoals[i][1].toInt()
                val max = userGoals[i][2].toInt()
                val metric = userGoals[i][3]
                val goalId = userGoals[i][4].toInt()

                val newWidget = WidgetGoalInfo(
                    goalId = goalId,
                    widgetText = text,
                    widgetProgress = progress,
                    widgetMax = max,
                    widgetMetric = metric
                )

                adapter.addWidget(newWidget)
            }
        }
        return adapter
    }
}