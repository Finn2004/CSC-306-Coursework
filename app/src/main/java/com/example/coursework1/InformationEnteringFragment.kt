package com.example.coursework1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class InformationEnteringFragment : Fragment() {

    private val userInfo = mutableListOf<String>()
    private lateinit var firstName : EditText
    private lateinit var surname : EditText
    private lateinit var dobDay : EditText
    private lateinit var dobMonth : EditText
    private lateinit var dobYear : EditText
    private lateinit var height : EditText
    private lateinit var weight : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_info, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstName = view.findViewById<EditText>(R.id.First_name)
        surname = view.findViewById<EditText>(R.id.Surname)
        dobDay = view.findViewById<EditText>(R.id.date_day)
        dobMonth = view.findViewById<EditText>(R.id.date_month)
        dobYear = view.findViewById<EditText>(R.id.date_year)
        height = view.findViewById<EditText>(R.id.Height)
        weight = view.findViewById<EditText>(R.id.Weight)
    }

    fun dobString (dobDay: String, dobMonth: String, dobYear: String) : String{
        return "$dobDay/$dobMonth/$dobYear"
    }

    fun getUserInfo() : MutableList<String> {
        userInfo.add(firstName.text.toString())
        userInfo.add(surname.text.toString())
        val dob = dobString(dobDay.text.toString(), dobMonth.text.toString(), dobYear.text.toString())
        userInfo.add(dob)
        userInfo.add(height.text.toString())
        userInfo.add(weight.text.toString())

        return userInfo
    }
}