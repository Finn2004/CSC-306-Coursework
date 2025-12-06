package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework1.adapter.WidgetDailyChallengesAdapter
import com.example.coursework1.adapter.WidgetDailyInfo
import com.example.coursework1.adapter.WidgetGoalAdapter
import com.example.coursework1.adapter.WidgetGoalInfo
import com.example.coursework1.adapter.WidgetWeeklyChallengesAdapter
import com.example.coursework1.adapter.WidgetWeeklyInfo

class GoalsAndChallengesActivity : AppCompatActivity() {
    private lateinit var goalAdapter: WidgetGoalAdapter
    private lateinit var goalAdapterSecondary: WidgetGoalAdapter
    private lateinit var dailyAdapter: WidgetDailyChallengesAdapter
    private lateinit var weeklyAdapter: WidgetWeeklyChallengesAdapter
    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var weeklyRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goals_and_challenges_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Background)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mToolbar = findViewById<Toolbar>(R.id.goals_top_toolbar)
        setSupportActionBar(mToolbar)

        goalAdapter = WidgetGoalAdapter { clickedGoal -> onWidgetGoalClicked(clickedGoal)}

        goalsRecyclerView = findViewById<RecyclerView>(R.id.goals_list)
        populateGoal(goalAdapter)
        goalsRecyclerView.adapter = goalAdapter
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)

        dailyAdapter = WidgetDailyChallengesAdapter { clickedChallenge -> onWidgetDailyClicked(clickedChallenge)}

        dailyRecyclerView = findViewById<RecyclerView>(R.id.daily_challenge_list)
        populateDaily(dailyAdapter)
        dailyRecyclerView.adapter = dailyAdapter
        dailyRecyclerView.layoutManager = LinearLayoutManager(this)

        weeklyAdapter = WidgetWeeklyChallengesAdapter { clickedChallenge -> onWidgetWeeklyClicked(clickedChallenge)}

        weeklyRecyclerView = findViewById<RecyclerView>(R.id.weekly_challenge_list)
        populateWeekly(weeklyAdapter)
        weeklyRecyclerView.adapter = weeklyAdapter
        weeklyRecyclerView.layoutManager = LinearLayoutManager(this)

        val homeButton = findViewById<ImageButton>(R.id.home_button)

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        val statisticsButton = findViewById<ImageButton>(R.id.statistics_button)

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
            R.id.add_goals_widget -> {
                val popUpLayout = layoutInflater.inflate(R.layout.add_goal_layout, null)
                val goal = mutableListOf<String>()

                val popUp = AlertDialog.Builder(this)
                    .setView(popUpLayout)
                    .setCancelable(true)
                    .setPositiveButton("Enter", null)
                    .setNegativeButton("Cancel", null)
                    .create()

                popUp.setOnShowListener {
                    popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                        val text = popUpLayout.findViewById<EditText>(R.id.Title)
                        goal.add(text.text.toString())
                        val target = popUpLayout.findViewById<EditText>(R.id.Goal)
                        goal.add(target.text.toString())
                        val metric = popUpLayout.findViewById<Spinner>(R.id.dropdown)
                        goal.add(metric.selectedItem.toString())
                        goal.add(userID.toString())

                        database.addUserGoal(goal)
                        goal.clear()

                        goalAdapterSecondary = WidgetGoalAdapter() { clickedGoal -> onWidgetGoalClickedSecondary(clickedGoal)}
                        goalAdapterSecondary = populateGoal(goalAdapterSecondary)
                        goalsRecyclerView.adapter = goalAdapterSecondary
                        popUp.dismiss()
                    }
                }

                popUp.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onWidgetGoalClicked(clickedGoal: WidgetGoalInfo) {
        val goalId = clickedGoal.goalId
        val database = UserDatabaseManager(this)

        val popUpLayout = layoutInflater.inflate(R.layout.goal_popup_layout, null)
        val text = popUpLayout.findViewById<TextView>(R.id.goal_text)
        val metric = popUpLayout.findViewById<TextView>(R.id.metric)
        val progress = popUpLayout.findViewById<TextView>(R.id.current_progress)
        val currentProgress = clickedGoal.widgetProgress.toString() + " / " + clickedGoal.widgetMax.toString()

        text.text = clickedGoal.widgetText
        progress.text = currentProgress
        metric.text = clickedGoal.widgetMetric

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

                if (progressEntered.toInt() + clickedGoal.widgetProgress >= clickedGoal.widgetMax) {
                    database.completeGoal(goalId)
                    goalAdapter.removeWidget(goalId)
                }
                else {
                    database.updateGoalProgress(goalId, progressEntered.toInt())
                    goalAdapter.updateProgress(goalId, progressEntered.toInt())
                }
                popUp.dismiss()

            }
        }

        popUp.show()
    }

    fun onWidgetDailyClicked(clickedChallenge: WidgetDailyInfo) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        val dailyId = clickedChallenge.dailyId

        val popUpLayout = layoutInflater.inflate(R.layout.goal_popup_layout, null)
        val text = popUpLayout.findViewById<TextView>(R.id.goal_text)
        val metric = popUpLayout.findViewById<TextView>(R.id.metric)
        val progress = popUpLayout.findViewById<TextView>(R.id.current_progress)
        val currentProgress = clickedChallenge.widgetProgress.toString() + " / " + clickedChallenge.widgetMax.toString()
        val image = popUpLayout.findViewById<ImageView>(R.id.image)

        text.text = clickedChallenge.widgetText
        progress.text = currentProgress
        metric.text = clickedChallenge.widgetMetric
        image.setImageResource(clickedChallenge.widgetImage)

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

                if ((progressEntered.toInt() + clickedChallenge.widgetProgress >= clickedChallenge.widgetMax) && !database.getDailyChallengeCompletedStatus(clickedChallenge.dailyId)) {
                    database.updateCurrentXP(userID, clickedChallenge.widgetXP)
                    database.updateDailyChallengeCompleted(clickedChallenge.dailyId)
                }

                database.updateDailyChallengeProgress(dailyId, progressEntered.toInt())
                dailyAdapter.updateProgress(dailyId, progressEntered.toInt())
                popUp.dismiss()

            }
        }

        popUp.show()
    }

    fun onWidgetWeeklyClicked(clickedChallenge: WidgetWeeklyInfo) {
        val weeklyId = clickedChallenge.weeklyId
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        val popUpLayout = layoutInflater.inflate(R.layout.goal_popup_layout, null)
        val text = popUpLayout.findViewById<TextView>(R.id.goal_text)
        val metric = popUpLayout.findViewById<TextView>(R.id.metric)
        val progress = popUpLayout.findViewById<TextView>(R.id.current_progress)
        val currentProgress = clickedChallenge.widgetProgress.toString() + " / " + clickedChallenge.widgetMax.toString()
        val image = popUpLayout.findViewById<ImageView>(R.id.image)

        text.text = clickedChallenge.widgetText
        progress.text = currentProgress
        metric.text = clickedChallenge.widgetMetric
        image.setImageResource(clickedChallenge.widgetImage)


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

                if ((progressEntered.toInt() + clickedChallenge.widgetProgress >= clickedChallenge.widgetMax) && !database.getWeeklyChallengeCompletedStatus(clickedChallenge.weeklyId)) {
                    database.updateCurrentXP(userID, clickedChallenge.widgetXP)
                    database.updateWeeklyChallengeCompleted(clickedChallenge.weeklyId)
                }

                database.updateWeeklyChallengeProgress(weeklyId, progressEntered.toInt())
                weeklyAdapter.updateProgress(weeklyId, progressEntered.toInt())
                popUp.dismiss()

            }
        }

        popUp.show()
    }

    fun onWidgetGoalClickedSecondary(clickedGoal: WidgetGoalInfo) {
        val goalId = clickedGoal.goalId
        val database = UserDatabaseManager(this)

        val popUpLayout = layoutInflater.inflate(R.layout.goal_popup_layout, null)
        val text = popUpLayout.findViewById<TextView>(R.id.goal_text)
        val metric = popUpLayout.findViewById<TextView>(R.id.metric)
        val progress = popUpLayout.findViewById<TextView>(R.id.current_progress)
        val currentProgress = clickedGoal.widgetProgress.toString() + " / " + clickedGoal.widgetMax.toString()

        text.text = clickedGoal.widgetText
        progress.text = currentProgress
        metric.text = clickedGoal.widgetMetric

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

                if (progressEntered.toInt() + clickedGoal.widgetProgress >= clickedGoal.widgetMax) {
                    database.completeGoal(goalId)
                    goalAdapterSecondary.removeWidget(goalId)
                }
                else {
                    database.updateGoalProgress(goalId, progressEntered.toInt())
                    goalAdapterSecondary.updateProgress(goalId, progressEntered.toInt())
                }
                popUp.dismiss()

            }
        }

        popUp.show()
    }

    fun populateGoal(adapter: WidgetGoalAdapter) : WidgetGoalAdapter {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)

        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))
        val userGoals = database.getUserGoals(userID)
        val userGoalsNum = userGoals.size

        for (i in 0 until userGoalsNum) {
            if (!database.getGoalCompletedStatus(userGoals[i][4].toInt())) {

                val text = userGoals[i][0]
                val progress = userGoals[i][1].toInt()
                val max = userGoals[i][2].toInt()
                val metric = userGoals[i][3]
                val goalId = userGoals[i][4].toInt()

                val newWidget = WidgetGoalInfo(
                    goalId = goalId,
                    widgetText = text,
                    widgetProgress = progress,
                    widgetMax = max,
                    widgetMetric = metric
                )

                adapter.addWidget(newWidget)
            }
        }
        return adapter
    }
    fun populateDaily(adapter: WidgetDailyChallengesAdapter) : WidgetDailyChallengesAdapter {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)

        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))
        val habits = database.getUserHabits(sharedPreferences.getString("User", null))

        for (i in 0 until habits.size) {

            if (database.habitForUser(userID, habits[i][0])) {

                val challenges = database.getDailyChallenges(habits[i][0], userID)
                val challengeNum = challenges.size

                for (i in 0 until challengeNum) {

                    val icon = database.getDefaultHabitInfo(challenges[i][5])[2].toInt()

                    val newChallengeWidget = WidgetDailyInfo(
                        dailyId = challenges[i][6].toInt(),
                        widgetText = challenges[i][0],
                        widgetProgress = challenges[i][1].toInt(),
                        widgetMax = challenges[i][2].toInt(),
                        widgetMetric = challenges[i][3],
                        widgetXP = challenges[i][4].toInt(),
                        widgetHabit = challenges[i][5],
                        widgetImage = icon
                    )

                    adapter.addWidget(newChallengeWidget)
                }
            }
        }

        return adapter
    }

    fun populateWeekly(adapter: WidgetWeeklyChallengesAdapter) : WidgetWeeklyChallengesAdapter {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)

        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))
        val habits = database.getUserHabits(sharedPreferences.getString("User", null))

        for (i in 0 until habits.size) {

            if (database.habitForUser(userID, habits[i][0])) {

                val challenges = database.getWeeklyChallenges(habits[i][0], userID)
                val challengeNum = challenges.size

                for (i in 0 until challengeNum) {

                    val icon = database.getDefaultHabitInfo(challenges[i][5])[2].toInt()

                    val newChallengeWidget = WidgetWeeklyInfo(
                        weeklyId = challenges[i][6].toInt(),
                        widgetText = challenges[i][0],
                        widgetProgress = challenges[i][1].toInt(),
                        widgetMax = challenges[i][2].toInt(),
                        widgetMetric = challenges[i][3],
                        widgetXP = challenges[i][4].toInt(),
                        widgetHabit = challenges[i][5],
                        widgetImage = icon
                    )

                    adapter.addWidget(newChallengeWidget)
                }
            }
        }

        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.goals_and_challenges_upper_bar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }
}