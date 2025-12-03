package com.example.coursework1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserProfileActivity : AppCompatActivity() {

    private lateinit var imageURI: Uri
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageURI = uri
            Log.d("photo picker", imageURI.toString())
        } else {
            Log.d("photo picker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val database = UserDatabaseManager(this)
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Background)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mToolbar = findViewById<Toolbar>(R.id.user_profile_screen_top_toolbar)
        setSupportActionBar(mToolbar)

        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        val profileCard = findViewById<CardView>(R.id.profile_card)
        val profileUsername = findViewById<TextView>(R.id.username)
        profileUsername.text = sharedPreferences.getString("User", null)
        val profileActive = findViewById<TextView>(R.id.ActiveTime)
        val date = "Active since: " + database.getUserJoinDate(userID)
        profileActive.text = date

        profileCard.setOnClickListener {
            val popUpLayout = layoutInflater.inflate(R.layout.profile_card_layout, null)

            val popUp = AlertDialog.Builder(this)
                .setView(popUpLayout)
                .setCancelable(true)
                .setPositiveButton("Enter", null)
                .setNeutralButton("Upload Image", null)
                .setNegativeButton("Cancel", null)
                .create()

            val profileImage = popUpLayout.findViewById<ImageView>(R.id.profile_image)

            popUp.setOnShowListener {
                popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                }

                popUp.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                    getImageSelector()
                    //profileImage.setImageURI(imageURI)
                }
            }

            popUp.show()
        }
    }

    fun getImageSelector() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.user_profile_upper_bar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }
}