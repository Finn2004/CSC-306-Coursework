package com.example.coursework1

import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.formatter.PercentFormatter


class PieChartFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_pie_chart, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(requireContext())
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        super.onViewCreated(view, savedInstanceState)

        val pieChart = view.findViewById<PieChart>(R.id.pie_chart)

        val groupLabels = getGroups(userID, database)
        val groupCount = groupLabels.size

        val entries = getHabitsEntries(userID, groupLabels, database)

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.sliceSpace = 1f

        val colours = getSliceColours(groupCount)
        pieDataSet.setColors(colours)

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieData.setValueTextSize(14f)
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueFormatter(PercentFormatter())

        pieChart.data = pieData
        pieChart.setExtraOffsets(20f,10f,20f,100f)

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)

        pieChart.isDrawHoleEnabled = false

        val legend = pieChart.legend
        legend.textColor = Color.WHITE
        legend.textSize = 16f
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.xOffset = 120f
        legend.yOffset = -80f

        pieChart.animateY(500)

    }

    fun getSliceColours(groupCount : Int) : MutableList<Int> {
        val colours = mutableListOf<Int>()
        val coloursToPick = listOf("#AA7CEB","#3D65F5","#C76542","#2DAD58","#E941FF","#34B7E7","#FB444A","#353434")

        for (i in 0 until groupCount) {
            colours.add(coloursToPick[i].toColorInt())
        }

        return colours
    }

    fun getGroups(userID: Int, database: UserDatabaseManager): List<String> {
        return database.getUserHabitNames(userID)
    }

    fun getHabitsEntries(userID: Int, groups: List<String>, database: UserDatabaseManager) : List<PieEntry> {
        val entries = mutableListOf<PieEntry>()
        for (i in 0 until groups.size) {
            val dailyVal = database.getUserCompletedDailyChallengesNumForHabit(userID, groups[i])
            val weeklyVal = database.getUserCompletedWeeklyChallengesNumForHabit(userID, groups[i])
            val habitVal = database.getUserCompletedDailyHabitNum(userID, groups[i])
            val entryVal = dailyVal + weeklyVal + habitVal
            val entry = PieEntry(entryVal.toFloat(), groups[i])
            entries.add(entry)
        }
        return entries
    }
}