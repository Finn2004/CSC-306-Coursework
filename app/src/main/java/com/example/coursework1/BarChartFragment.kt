package com.example.coursework1

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BarChartFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_bar_chart, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)

        val groupLabels = getGroups(userID, database)
        val groupCount = groupLabels.size

        val dataSet1 = BarDataSet(getDailyCompletesEntries(userID, groupLabels, database), "Daily")
        dataSet1.color = "#50C878".toColorInt()
        val dataSet2 = BarDataSet(getWeeklyCompletesEntries(userID, groupLabels, database), "Weekly")
        dataSet2.color = "#EFBF04".toColorInt()

        val barDataSet = BarData(dataSet1, dataSet2)
        barDataSet.barWidth = 0.3f

        barChart.data = barDataSet
        barChart.animateY(500)

        val groupSpace = 0.2f
        val barSpace = 0.05f
        val startX = 0f

        val groupWidth = barDataSet.getGroupWidth(groupSpace, barSpace)
        barChart.xAxis.axisMinimum = startX
        barChart.xAxis.axisMaximum = startX + groupWidth * groupCount

        barChart.groupBars(startX, groupSpace, barSpace)

        val xAxis = barChart.xAxis
        xAxis.textColor = Color.WHITE
        xAxis.textSize = 12f
        xAxis.gridColor = Color.GRAY
        xAxis.axisLineColor = Color.WHITE
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.labelRotationAngle = -45f
        xAxis.setCenterAxisLabels(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(groupLabels)

        val leftAxis = barChart.axisLeft
        leftAxis.textColor = Color.WHITE
        leftAxis.gridColor = Color.DKGRAY
        leftAxis.axisLineColor = Color.WHITE

        val rightAxis = barChart.axisRight
        rightAxis.textColor = Color.WHITE
        rightAxis.gridColor = Color.DKGRAY
        rightAxis.axisLineColor = Color.WHITE

        val legend = barChart.legend
        legend.textColor = Color.WHITE
        legend.textSize = 12f
    }

    fun getGroups(userID: Int, database: UserDatabaseManager): List<String> {
        return database.getUserHabitNames(userID)
    }

    fun getDailyCompletesEntries(userID: Int, groups: List<String>, database: UserDatabaseManager) : List<BarEntry> {
        val entries = mutableListOf<BarEntry>()
        for (i in 0 until groups.size) {
            val entryVal = database.getUserCompletedDailyChallengesNumForHabit(userID, groups[i])
            val entry = BarEntry(i.toFloat(), entryVal.toFloat())
            entries.add(entry)
        }
        return entries
    }

    fun getWeeklyCompletesEntries(userID: Int, groups: List<String>, database: UserDatabaseManager) : List<BarEntry> {
        val entries = mutableListOf<BarEntry>()
        for (i in 0 until groups.size) {
            val entryVal = database.getUserCompletedWeeklyChallengesNumForHabit(userID, groups[i])
            val entry = BarEntry(i.toFloat(), entryVal.toFloat())
            entries.add(entry)
        }
        return entries
    }
}