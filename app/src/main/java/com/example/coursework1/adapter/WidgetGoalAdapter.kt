package com.example.coursework1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.coursework1.R
import com.google.android.material.progressindicator.LinearProgressIndicator

data class WidgetGoalInfo (
    val goalId: Int,
    val widgetText: String,
    var widgetProgress: Int,
    val widgetMax: Int,
    val widgetMetric: String
)

class WidgetGoalAdapter(private val widgetButton: (WidgetGoalInfo) -> Unit) : RecyclerView.Adapter<WidgetGoalAdapter.WidgetViewHolder>() {
    private var layouts = mutableListOf<WidgetGoalInfo>()

    inner class WidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val text = view.findViewById<TextView>(R.id.text)
        private val progress = view.findViewById<LinearProgressIndicator>(R.id.progress_bar)
        private val progressInfo = view.findViewById<TextView>(R.id.current_progress)

        fun bind(info: WidgetGoalInfo) {

            text.text = info.widgetText

            progress.isIndeterminate = false
            progress.max = info.widgetMax
            progress.setProgress(info.widgetProgress, true)

            val progressInfoText = info.widgetProgress.toString() + " / " + info.widgetMax
            progressInfo.text = progressInfoText

            itemView.setOnClickListener {
                widgetButton(info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val widgetView = LayoutInflater.from(parent.context).inflate(R.layout.goal_layout, parent, false)
        return WidgetViewHolder(widgetView)
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(layouts[position])
    }

    fun addWidget(layout: WidgetGoalInfo) {
        layouts.add(layout)
        notifyItemInserted(layouts.size - 1)
    }

    fun removeWidget(layout: WidgetGoalInfo) {
        val position = layouts.indexOf(layout)
        layouts.remove(layout)
        notifyItemRemoved(position)
    }

    fun updateProgress(id: Int, progress: Int) {
        val position = layouts.indexOfFirst { it.goalId == id }
        val newProgress = progress + layouts[position].widgetProgress

        layouts[position] = layouts[position].copy(widgetProgress = newProgress)
        notifyItemChanged(position)
    }
}