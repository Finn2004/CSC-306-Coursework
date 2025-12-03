package com.example.coursework1.adapter

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.coursework1.R
import com.google.android.material.progressindicator.CircularProgressIndicator

data class WidgetHabitInfo (
    val habitId: Int,
    val widgetText: String,
    var widgetProgress: Int,
    val widgetMax: Int,
    val widgetImage: Int,
    val widgetIndicatorColour: Int,
    val widgetTrackColour: Int
)

class WidgetHabitAdapter(private val widgetButton: (WidgetHabitInfo) -> Unit) : RecyclerView.Adapter<WidgetHabitAdapter.WidgetViewHolder>() {
    private var layouts = mutableListOf<WidgetHabitInfo>()

    inner class WidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.text)
        private val progress = view.findViewById<CircularProgressIndicator>(R.id.progress_bar)
        private val image = view.findViewById<ImageView>(R.id.image)

        fun bind(info: WidgetHabitInfo) {
            text.text = info.widgetText

            progress.isIndeterminate = false
            progress.max = info.widgetMax
            progress.setProgress(info.widgetProgress, true)
            progress.setIndicatorColor(info.widgetIndicatorColour)
            progress.trackColor = info.widgetTrackColour

            image.setImageResource(info.widgetImage)

            itemView.setOnClickListener {
                widgetButton(info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val widgetView = LayoutInflater.from(parent.context).inflate(R.layout.habit_widget_layout, parent, false)
        return WidgetViewHolder(widgetView)
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(layouts[position])
    }

    fun addWidget(layout: WidgetHabitInfo) {
        layouts.add(layout)
        val position = layouts.size - 1
        notifyItemInserted(position)
        notifyItemRangeChanged(position, layouts.size - position)
    }

    fun removeWidget(layout: WidgetHabitInfo) {
        val position = layouts.indexOf(layout)
        layouts.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, layouts.size - position)
    }

    fun updateProgress(id: Int, progress: Int) {
        val position = layouts.indexOfFirst { it.habitId == id }
        val newProgress = progress + layouts[position].widgetProgress

        layouts[position] = layouts[position].copy(widgetProgress = newProgress)
        notifyItemChanged(position)
    }
}