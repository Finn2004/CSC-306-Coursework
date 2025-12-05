package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework1.adapter.WidgetHabitAdapter
import com.example.coursework1.adapter.WidgetHabitInfo

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var adapter: WidgetHabitAdapter
    private lateinit var adapterSecondary: WidgetHabitAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val database = UserDatabaseManager(this)
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))
        database.updateAchievements(userID)
        database.setUserDailyChallenges(userID)
        database.setUserWeeklyChallenges(userID)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Background)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mToolbar = findViewById<Toolbar>(R.id.home_screen_top_toolbar)
        setSupportActionBar(mToolbar)

        adapter = WidgetHabitAdapter { clickedHabit -> onWidgetClicked(clickedHabit)}
        populate(adapter)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_home)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val goalsAndChallengesButton = findViewById<ImageButton>(R.id.goals_button)

        val statisticsButton = findViewById<ImageButton>(R.id.statistics_button)

        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        goalsAndChallengesButton.setOnClickListener {
            val intent = Intent(this, GoalsAndChallengesActivity::class.java)
            startActivity(intent)
        }

        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val database = UserDatabaseManager(this)
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val user = sharedPreferences.getString("User", null)
        val userID = database.getUserIdByUsername(user)

        when (item.itemId) {
            R.id.add_widget -> {
                val popUpLayout = layoutInflater.inflate(R.layout.edit_habits_layout, null)
                val habits = mutableListOf<ToggleButton>()

                val popUp = AlertDialog.Builder(this)
                    .setView(popUpLayout)
                    .setCancelable(true)
                    .setPositiveButton("Enter", null)
                    .setNegativeButton("Cancel", null)
                    .create()

                popUp.setOnShowListener {
                    popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                        val waterIntakeButton = popUpLayout.findViewById<ToggleButton>(R.id.water_intake_button)
                        habits.add(waterIntakeButton)
                        val fiveADayButton = popUpLayout.findViewById<ToggleButton>(R.id.five_a_day_button)
                        habits.add(fiveADayButton)
                        val calorieIntakeButton = popUpLayout.findViewById<ToggleButton>(R.id.calorie_intake_button)
                        habits.add(calorieIntakeButton)
                        val walkingButton = popUpLayout.findViewById<ToggleButton>(R.id.walking_button)
                        habits.add(walkingButton)
                        val cyclingButton = popUpLayout.findViewById<ToggleButton>(R.id.cycling_button)
                        habits.add(cyclingButton)
                        val sleepButton = popUpLayout.findViewById<ToggleButton>(R.id.sleep_button)
                        habits.add(sleepButton)
                        val exerciseButton = popUpLayout.findViewById<ToggleButton>(R.id.exercise_button)
                        habits.add(exerciseButton)
                        val swimmingButton = popUpLayout.findViewById<ToggleButton>(R.id.swimming_button)
                        habits.add(swimmingButton)

                        for (habit in habits) {

                            if (habit.isChecked) {

                                val habitName = habit.textOn.toString()
                                database.addHabitIfNotPresent(userID, habitName)
                            }
                            else {
                                val habitName = habit.textOff.toString()
                                database.removeHabitIfPresent(userID, habitName)
                            }
                        }
                        adapterSecondary = WidgetHabitAdapter() {clickedHabit -> onWidgetClickedSecondary(clickedHabit)}
                        adapterSecondary = populate(adapterSecondary)
                        recyclerView.adapter = adapterSecondary
                        popUp.dismiss()
                    }
                }

                popUp.show()
                return true
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onWidgetClicked(clickedHabit: WidgetHabitInfo) {
        val habitId = clickedHabit.habitId
        val database = UserDatabaseManager(this)

        val popUpLayout = layoutInflater.inflate(R.layout.habit_popup_layout, null)
        val text = popUpLayout.findViewById<TextView>(R.id.text)
        text.text = clickedHabit.widgetText
        val metric = popUpLayout.findViewById<TextView>(R.id.metric)
        metric.text = database.getHabitMetric(habitId)
        val icon = popUpLayout.findViewById<ImageView>(R.id.image)
        icon.setImageResource(clickedHabit.widgetImage)
        val progress = popUpLayout.findViewById<TextView>(R.id.current_progress)
        val currentProgress = clickedHabit.widgetProgress.toString() + " / " + clickedHabit.widgetMax.toString()
        progress.text = currentProgress

        val progressEnterable = popUpLayout.findViewById<EditText>(R.id.progress_enterable)

        val popUp = AlertDialog.Builder(this)
            .setView(popUpLayout)
            .setCancelable(true)
            .setPositiveButton("Enter", null)
            .setNegativeButton("Cancel", null)
            .create()

        popUp.setOnShowListener {
            popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                var progressEntered = progressEnterable.text.toString()

                if (progressEntered == "") {
                    progressEntered = "0"
                }

                database.updateHabitProgress(habitId, progressEntered.toInt())
                adapter.updateProgress(habitId, progressEntered.toInt())
                popUp.dismiss()

            }
        }

        popUp.show()
    }

    fun onWidgetClickedSecondary(clickedHabit: WidgetHabitInfo) {
        val habitId = clickedHabit.habitId
        val database = UserDatabaseManager(this)

        val popUpLayout = layoutInflater.inflate(R.layout.habit_popup_layout, null)
        val text = popUpLayout.findViewById<TextView>(R.id.text)
        text.text = clickedHabit.widgetText
        val metric = popUpLayout.findViewById<TextView>(R.id.metric)
        metric.text = database.getHabitMetric(habitId)
        val icon = popUpLayout.findViewById<ImageView>(R.id.image)
        icon.setImageResource(clickedHabit.widgetImage)
        val progress = popUpLayout.findViewById<TextView>(R.id.current_progress)
        val currentProgress = clickedHabit.widgetProgress.toString() + " / " + clickedHabit.widgetMax.toString()
        progress.text = currentProgress

        val progressEnterable = popUpLayout.findViewById<EditText>(R.id.progress_enterable)

        val popUp = AlertDialog.Builder(this)
            .setView(popUpLayout)
            .setCancelable(true)
            .setPositiveButton("Enter", null)
            .setNegativeButton("Cancel", null)
            .create()

        popUp.setOnShowListener {
            popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                var progressEntered = progressEnterable.text.toString()

                if (progressEntered == "") {
                    progressEntered = "0"
                }

                database.updateHabitProgress(habitId, progressEntered.toInt())
                adapterSecondary.updateProgress(habitId, progressEntered.toInt())
                popUp.dismiss()

            }
        }

        popUp.show()
    }

    fun populate(adapter: WidgetHabitAdapter) : WidgetHabitAdapter {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)

        val userHabits = database.getUserHabits(sharedPreferences.getString("User", null))
        val userHabitNum = userHabits.size

        for (i in 0 until userHabitNum) {
            val text = userHabits[i][0]
            val progress = userHabits[i][1].toInt()
            val max = userHabits[i][2].toInt()
            val image = userHabits[i][4].toInt()
            val habitId = userHabits[i][5].toInt()

            val habitNames = resources.getStringArray(R.array.progress_name)
            val indicatorColours = resources.obtainTypedArray(R.array.progress_indicator_colour)
            val trackColours = resources.obtainTypedArray(R.array.progress_track_colour)

            val position = habitNames.indexOf(text)
            val indicatorColour = ContextCompat.getColor(this, indicatorColours.getResourceId(position, 0))
            val trackColour = ContextCompat.getColor(this, trackColours.getResourceId(position, 0))

            val newWidget = WidgetHabitInfo(habitId = habitId,
                widgetText = text,
                widgetProgress = progress,
                widgetMax = max,
                widgetImage = image,
                widgetIndicatorColour = indicatorColour,
                widgetTrackColour = trackColour)

            adapter.addWidget(newWidget)
            indicatorColours.recycle()
            trackColours.recycle()
        }
        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.home_screen_upper_appbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }
}