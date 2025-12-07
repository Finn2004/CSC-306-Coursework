package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.coursework1.adapter.StatsTabsAdapter
import android.widget.TextView

class CompetitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_competition)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Background)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mToolbar = findViewById<Toolbar>(R.id.comp_top_toolbar)
        setSupportActionBar(mToolbar)


        val goalsAndChallengesButton = findViewById<ImageButton>(R.id.goals_button)

        goalsAndChallengesButton.setOnClickListener {
            val intent = Intent(this, GoalsAndChallengesActivity::class.java)
            startActivity(intent)
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.statistics_upper_bar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

}