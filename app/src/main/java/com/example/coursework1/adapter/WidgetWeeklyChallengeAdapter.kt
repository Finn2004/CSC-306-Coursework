package com.example.coursework1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView

import com.example.coursework1.R
import com.google.android.material.progressindicator.LinearProgressIndicator

data class WidgetWeeklyInfo (
    val weeklyId: Int,
    val widgetText: String,
    var widgetProgress: Int,
    val widgetMax: Int,
    val widgetMetric: String,
    val widgetXP: Int,
    val widgetHabit: String,
    val widgetImage: Int
)

class WidgetWeeklyChallengesAdapter(private val widgetButton: (WidgetWeeklyInfo) -> Unit) : RecyclerView.Adapter<WidgetWeeklyChallengesAdapter.WidgetViewHolder>() {
    private var layouts = mutableListOf<WidgetWeeklyInfo>()

    inner class WidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val text = view.findViewById<TextView>(R.id.challenge_text)
        private val progressBar = view.findViewById<LinearProgressIndicator>(R.id.challenge_progress_bar)
        private val xp = view.findViewById<TextView>(R.id.challenge_XP)
        private val progressInfo = view.findViewById<TextView>(R.id.challenge_progress)

        fun bind(info: WidgetWeeklyInfo) {
            text.text = info.widgetText
            val imageIcon = AppCompatResources.getDrawable(itemView.context, info.widgetImage)
            imageIcon?.setBounds(0,0,96,96)
            text.setCompoundDrawablesRelative(null,null,imageIcon,null)

            progressBar.isIndeterminate = false
            progressBar.max = info.widgetMax
            progressBar.setProgress(info.widgetProgress, true)

            val challengeXP = info.widgetXP.toString() + "XP"
            xp.text = challengeXP

            val progressAndMetric = info.widgetProgress.toString() + " / " + info.widgetMax.toString() + " " + info.widgetMetric
            progressInfo.text = progressAndMetric

            itemView.setOnClickListener {
                widgetButton(info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val widgetView = LayoutInflater.from(parent.context).inflate(R.layout.weekly_challenge_layout, parent, false)
        return WidgetViewHolder(widgetView)
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(layouts[position])
    }

    fun addWidget(layout: WidgetWeeklyInfo) {
        layouts.add(layout)
        notifyItemInserted(layouts.size - 1)
    }

    fun removeWidget(layout: WidgetWeeklyInfo) {
        val position = layouts.indexOf(layout)
        layouts.remove(layout)
        notifyItemRemoved(position)
    }

    fun updateProgress(id: Int, progress: Int) {
        val position = layouts.indexOfFirst { it.weeklyId == id }
        val newProgress = progress + layouts[position].widgetProgress

        layouts[position] = layouts[position].copy(widgetProgress = newProgress)
        notifyItemChanged(position)
    }
}