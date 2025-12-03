package com.example.coursework1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment

class HabitSelectionFragment : Fragment() {

    private val habits = mutableListOf<ToggleButton>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_habit, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val waterIntakeButton = view.findViewById<ToggleButton>(R.id.water_intake_button)
        habits.add(waterIntakeButton)
        val fiveADayButton = view.findViewById<ToggleButton>(R.id.five_a_day_button)
        habits.add(fiveADayButton)
        val calorieIntakeButton = view.findViewById<ToggleButton>(R.id.calorie_intake_button)
        habits.add(calorieIntakeButton)
        val walkingButton = view.findViewById<ToggleButton>(R.id.walking_button)
        habits.add(walkingButton)
        val cyclingButton = view.findViewById<ToggleButton>(R.id.cycling_button)
        habits.add(cyclingButton)
        val sleepButton = view.findViewById<ToggleButton>(R.id.sleep_button)
        habits.add(sleepButton)
        val exerciseButton = view.findViewById<ToggleButton>(R.id.exercise_button)
        habits.add(exerciseButton)
        val swimmingButton = view.findViewById<ToggleButton>(R.id.swimming_button)
        habits.add(swimmingButton)
    }

    fun getToggles() :  List<String>{

        val selected = mutableListOf<String>()

        for (habit in habits) {
            if (habit.isChecked) {
                selected.add(habit.textOn.toString())
            }
        }

        return selected
    }
}