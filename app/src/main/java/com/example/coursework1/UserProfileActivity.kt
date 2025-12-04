package com.example.coursework1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ContentView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.coursework1.adapter.AccountTabsAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import java.security.Security

class UserProfileActivity : AppCompatActivity() {

    private var imageURI: Uri? = null
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageURI = uri
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

        val profileBio = findViewById<TextView>(R.id.profile_bio)
        val bio = "Bio: " + database.getUserBio(userID)
        profileBio.text = bio

        val uri = database.getUserProfilePicture(userID)
        if (uri != null) {
            val profileImage = findViewById<ImageView>(R.id.profile_image)
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            profileImage.setImageURI(uri)
        }

        profileCard.setOnClickListener {
            val popUpLayout = layoutInflater.inflate(R.layout.profile_card_layout, null)

            val currentBio = database.getUserBio(userID)
            popUpLayout.findViewById<EditText>(R.id.popup_profile_bio).setText(currentBio)

            val currentActiveRange = "Active since: " + database.getUserJoinDate(userID)
            popUpLayout.findViewById<TextView>(R.id.Activity_date).text = currentActiveRange

            if (uri != null) {
                val cardImage = popUpLayout.findViewById<ImageView>(R.id.profile_image)
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                cardImage.setImageURI(uri)
            }

            val popUp = AlertDialog.Builder(this)
                .setView(popUpLayout)
                .setCancelable(true)
                .setPositiveButton("Enter", null)
                .setNeutralButton("Upload Image", null)
                .setNegativeButton("Cancel", null)
                .create()

            popUp.setOnShowListener {
                popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val bio = popUpLayout.findViewById<EditText>(R.id.popup_profile_bio)
                    database.updateUserBio(userID, bio.text.toString())

                    val uri = imageURI
                    if (uri != null) {
                        database.updateUserProfilePicture(userID, uri)
                        val profileImage = findViewById<ImageView>(R.id.profile_image)
                        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        profileImage.setImageURI(uri)
                    }

                    findViewById<TextView>(R.id.profile_bio).text = bio.text.toString()

                    popUp.dismiss()
                }

                popUp.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                    getImageSelector()
                    val cardImage = popUpLayout.findViewById<ImageView>(R.id.profile_image)
                    val uri = imageURI

                    if (uri != null) {
                        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        cardImage.setImageURI(uri)
                    }
                }
            }

            popUp.show()
        }

        val tabs = findViewById<MaterialButtonToggleGroup>(R.id.Tab_buttons)

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = AccountTabsAdapter(this)

        tabs.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.left_tab -> {
                        viewPager.setCurrentItem(0, true)
                    }
                    R.id.right_tab -> {
                        viewPager.setCurrentItem(1, true)
                    }
                }
            }
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