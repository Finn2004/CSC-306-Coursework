package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coursework1.adapter.WidgetHabitAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val database = UserDatabaseManager(this)
        val userID = database.getUserIdByUsername(sharedPreferences.getString("User", null))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mToolbar = findViewById<Toolbar>(R.id.settings_top_toolbar)
        setSupportActionBar(mToolbar)

        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        val changeEmail = findViewById<CardView>(R.id.Change_Email)
        changeEmail.setOnClickListener {
            val popUpLayout = layoutInflater.inflate(R.layout.email_change_layout, null)

            val popUp = AlertDialog.Builder(this)
                .setView(popUpLayout)
                .setCancelable(true)
                .setPositiveButton("Enter", null)
                .setNegativeButton("Cancel", null)
                .create()

            popUp.setOnShowListener {
                popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val oldEmail = popUpLayout.findViewById<EditText>(R.id.old_email)
                    val password = popUpLayout.findViewById<EditText>(R.id.password)
                    val newEmail = popUpLayout.findViewById<EditText>(R.id.new_email)

                    if (database.verifyChanges(userID, oldEmail.text.toString(), password.text.toString()))  {
                        database.updateUserEmail(userID, newEmail.text.toString())
                        popUp.dismiss()
                    }
                    else {
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                    }


                }
            }

            popUp.show()
        }

        val changePassword = findViewById<CardView>(R.id.Change_Password)
        changePassword.setOnClickListener {
            val popUpLayout = layoutInflater.inflate(R.layout.password_change_layout, null)

            val popUp = AlertDialog.Builder(this)
                .setView(popUpLayout)
                .setCancelable(true)
                .setPositiveButton("Enter", null)
                .setNegativeButton("Cancel", null)
                .create()

            popUp.setOnShowListener {
                popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val email = popUpLayout.findViewById<EditText>(R.id.email)
                    val oldPassword = popUpLayout.findViewById<EditText>(R.id.old_password)
                    val newPassword = popUpLayout.findViewById<EditText>(R.id.new_password)

                    if (database.verifyChanges(userID, email.text.toString(), oldPassword.text.toString()))  {
                        database.updateUserPassword(userID, newPassword.text.toString())
                        popUp.dismiss()
                    }
                    else {
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                    }


                }
            }

            popUp.show()
        }

        val private = "Account Privacy: Private"
        val public = "Account Privacy: Public"

        val switch = findViewById<SwitchMaterial>(R.id.privacy_switch)
        val privacyText = findViewById<TextView>(R.id.Privacy_text)

        if (database.getUserPrivacySetting(userID) == "Private") {
            switch.isChecked = true
            privacyText.text = private
            database.updateUserPrivacySetting(userID, "Private")
        }
        else {
            switch.isChecked = false
            privacyText.text = public
            database.updateUserPrivacySetting(userID, "Public")
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                privacyText.text = private
            }
            else {
                privacyText.text = public
            }
        }

        val deleteData = findViewById<CardView>(R.id.Delete_Personal_Data)
        deleteData.setOnClickListener {
            val popUpLayout = layoutInflater.inflate(R.layout.confirm_changes_layout, null)

            val popUp = AlertDialog.Builder(this)
                .setView(popUpLayout)
                .setCancelable(true)
                .setPositiveButton("Enter", null)
                .setNegativeButton("Cancel", null)
                .create()

            popUp.setOnShowListener {
                popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val email = popUpLayout.findViewById<EditText>(R.id.email)
                    val password = popUpLayout.findViewById<EditText>(R.id.password)

                    if (database.verifyChanges(userID, email.text.toString(), password.text.toString()))  {
                        database.deleteUserData(userID)
                        Snackbar.make(findViewById(android.R.id.content), "Data Deleted", Snackbar.LENGTH_LONG).show()
                        popUp.dismiss()
                    }
                    else {
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                    }


                }
            }

            popUp.show()
        }

        val deleteAccount = findViewById<CardView>(R.id.Delete_Account)
        deleteAccount.setOnClickListener {
            val popUpLayout = layoutInflater.inflate(R.layout.confirm_changes_layout, null)

            val popUp = AlertDialog.Builder(this)
                .setView(popUpLayout)
                .setCancelable(true)
                .setPositiveButton("Enter", null)
                .setNegativeButton("Cancel", null)
                .create()

            popUp.setOnShowListener {
                popUp.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val email = popUpLayout.findViewById<EditText>(R.id.email)
                    val password = popUpLayout.findViewById<EditText>(R.id.password)

                    if (database.verifyChanges(userID, email.text.toString(), password.text.toString()))  {
                        database.deleteUser(userID)
                        Snackbar.make(findViewById(android.R.id.content), "Data Deleted", Snackbar.LENGTH_LONG).show()
                        popUp.dismiss()

                        editor.putBoolean("logged_in", false)
                        editor.remove("user")
                        editor.apply()

                        val intent = Intent(this, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                    }


                }
            }

            popUp.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.user_profile_upper_bar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }
}