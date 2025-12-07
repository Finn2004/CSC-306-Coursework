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
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BarChartHorizontalFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_bar_chart_horizontal, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<HorizontalBarChart>(R.id.barChartHorizonal)

        val groupLabels = getGroups(userID, database)
        val groupCount = groupLabels.size

        val dataSet1 = BarDataSet(getDailyHabitsEntries(userID, groupLabels, database), "Daily")
        dataSet1.color = "#AA7CEB".toColorInt()

        val barDataSet = BarData(dataSet1)
        barDataSet.barWidth = 0.4f

        barChart.data = barDataSet
        barChart.animateY(500)

        val xAxis = barChart.xAxis
        xAxis.textColor = Color.WHITE
        xAxis.textSize = 20f
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.gridColor = Color.GRAY
        xAxis.axisLineColor = Color.WHITE
        xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        xAxis.labelRotationAngle = 0f
        xAxis.setCenterAxisLabels(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(groupLabels)

        val leftAxis = barChart.axisLeft
        leftAxis.textColor = Color.WHITE
        leftAxis.gridColor = Color.DKGRAY
        leftAxis.axisLineColor = Color.WHITE
        leftAxis.granularity = 1f
        leftAxis.isGranularityEnabled = true

        val rightAxis = barChart.axisRight
        rightAxis.textColor = Color.WHITE
        rightAxis.gridColor = Color.DKGRAY
        rightAxis.axisLineColor = Color.WHITE
        rightAxis.granularity = 1f
        rightAxis.isGranularityEnabled = true

        val legend = barChart.legend
        legend.textColor = Color.WHITE
        legend.textSize = 12f
    }

    fun getGroups(userID: Int, database: UserDatabaseManager): List<String> {
        return database.getUserHabitNames(userID)
    }

    fun getDailyHabitsEntries(userID: Int, groups: List<String>, database: UserDatabaseManager) : List<BarEntry> {
        val entries = mutableListOf<BarEntry>()
        for (i in 0 until groups.size) {
            val entryVal = database.getUserCompletedDailyHabitNum(userID, groups[i])
            val entry = BarEntry(i.toFloat(), entryVal.toFloat())
            entries.add(entry)
        }
        return entries
    }
}