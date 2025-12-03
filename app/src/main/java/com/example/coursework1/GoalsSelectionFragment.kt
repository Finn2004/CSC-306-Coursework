package com.example.coursework1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment

class GoalsSelectionFragment : Fragment() {

    private val userGoal = mutableListOf<String>()
    private val userGoals : MutableList<MutableList<String>> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_goals, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goalDesc = view.findViewById<EditText>(R.id.Title)
        val goalAmount = view.findViewById<EditText>(R.id.Goal)
        val metric = view.findViewById<Spinner>(R.id.dropdown)
        val saveButton = view.findViewById<Button>(R.id.SaveButton)

        saveButton.setOnClickListener {
            userGoal.add(goalDesc.text.toString())
            userGoal.add(goalAmount.text.toString())
            userGoal.add(metric.selectedItem.toString())
            userGoals.add(userGoal.toMutableList())
            userGoal.clear()

            goalDesc.text.clear()
            goalAmount.text.clear()
        }
    }

    fun getUserGoals() : MutableList<MutableList<String>>{
        return userGoals
    }
}