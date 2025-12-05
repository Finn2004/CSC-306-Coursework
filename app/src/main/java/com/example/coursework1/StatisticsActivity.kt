package com.example.coursework1

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StatisticsActivity : AppCompatActivity(), SensorEventListener {

    private  var sensorManager: SensorManager? = null
    private var totalSteps = 0f
    private var previousSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Background)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val mToolbar = findViewById<Toolbar>(R.id.stats_top_toolbar)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.statistics_upper_bar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        val stepCounter = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(this,sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
            SensorManager.SENSOR_DELAY_NORMAL)
        super.onResume()
    }

    override fun onPause() {
        sensorManager?.unregisterListener(this)
        super.onPause()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
            val currentSteps = totalSteps.toInt() - previousSteps.toInt()
            val text = findViewById<TextView>(R.id.steps)
            text.text = currentSteps.toString()

        }
    }
}