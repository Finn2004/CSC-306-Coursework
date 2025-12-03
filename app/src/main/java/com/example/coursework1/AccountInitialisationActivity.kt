package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.coursework1.adapter.TabsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AccountInitialisationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_initialisation)

        val mToolbar = findViewById<Toolbar>(R.id.initialisation_toolbar)
        setSupportActionBar(mToolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.pager)

        val tabTitles = arrayOf("Habits","Goals","Information")

        viewPager.adapter = TabsAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            when (position) {
                0 -> tab.text = tabTitles[0]
                1 -> tab.text = tabTitles[1]
                2 -> tab.text = tabTitles[2]
            }
        }.attach()

        val initialise = findViewById<Button>(R.id.FinishButton)

        initialise.setOnClickListener {

            val habitFragment =
                supportFragmentManager.findFragmentByTag("f0") as? HabitSelectionFragment
            val goalFragment =
                supportFragmentManager.findFragmentByTag("f1") as? GoalsSelectionFragment
            val infoFragment =
                supportFragmentManager.findFragmentByTag("f2") as? InformationEnteringFragment

            val user = sharedPreferences.getString("User", null)
            val userId = database.getUserIdByUsername(user)

            val toggles = habitFragment?.getToggles()

            if (toggles != null) {
                for (toggle in toggles) {
                    var habitMax = 0
                    var habitMetric = "none"
                    var habitIcon = R.drawable.ic_account

                    when (toggle) {
                        "Daily Water Intake" -> {
                            habitMax = 2500
                            habitMetric = "ml"
                            habitIcon = R.drawable.ic_water
                        }
                        "5-a-day" -> {
                            habitMax = 5
                            habitMetric = "none"
                            habitIcon = R.drawable.ic_5_a_day
                        }
                        "Daily Calorie Intake" -> {
                            habitMax = 2250
                            habitMetric = "kCal"
                            habitIcon = R.drawable.ic_calorie
                        }
                        "Walking Distance" -> {
                            habitMax = 10000
                            habitMetric = "steps"
                            habitIcon = R.drawable.ic_walking
                        }
                        "Cycling Distance" -> {
                            habitMax = 12
                            habitMetric = "miles"
                            habitIcon = R.drawable.ic_cycle
                        }
                        "Sleep monitor" -> {
                            habitMax = 8
                            habitMetric = "hours"
                            habitIcon = R.drawable.ic_sleep
                        }
                        "Exercise Duration" -> {
                            habitMax = 45
                            habitMetric = "minutes"
                            habitIcon = R.drawable.ic_exercise
                        }
                        "Swimming Duration" -> {
                            habitMax = 2000
                            habitMetric = "metres"
                            habitIcon = R.drawable.ic_swim
                        }
                        else -> {}
                    }

                    database.addUserHabit(toggle, habitMax, habitMetric, habitIcon, userId)
                }
            }

            val goals = goalFragment?.getUserGoals()

            if (goals != null) {
                for (goal in goals) {
                    goal.add(userId.toString())
                    database.addUserGoal(goal)
                }
            }

            val info = infoFragment?.getUserInfo()

            if (info != null) {
                info.add(userId.toString())
                database.addUserInfo(info)
            }

            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)

        }
    }
}