package com.example.coursework1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.coursework1.R
import com.google.android.material.progressindicator.LinearProgressIndicator

data class WidgetAchievementInfo (
    val achievementId: Int,
    val widgetText: String,
    var widgetProgress: Int,
    val widgetMax: Int,
    val widgetMetric: String
)

class WidgetAchievementAdapter(private val widgetButton: (WidgetAchievementInfo) -> Unit) : RecyclerView.Adapter<WidgetAchievementAdapter.WidgetViewHolder>() {
    private var layouts = mutableListOf<WidgetAchievementInfo>()

    inner class WidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val text = view.findViewById<TextView>(R.id.text)
        private val progress = view.findViewById<LinearProgressIndicator>(R.id.progress_bar)
        private val progressInfo = view.findViewById<TextView>(R.id.current_progress)

        fun bind(info: WidgetAchievementInfo) {

            text.text = info.widgetText

            progress.isIndeterminate = false
            progress.max = info.widgetMax
            progress.setProgress(info.widgetProgress, true)

            val progressInfoText = info.widgetProgress.toString() + " / " + info.widgetMax + " " + info.widgetMetric
            progressInfo.text = progressInfoText

            itemView.setOnClickListener {
                widgetButton(info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val widgetView = LayoutInflater.from(parent.context).inflate(R.layout.achievement_widget_layout, parent, false)
        return WidgetViewHolder(widgetView)
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(layouts[position])
    }

    fun addWidget(layout: WidgetAchievementInfo) {
        layouts.add(layout)
        notifyItemInserted(layouts.size - 1)
    }
}