package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val database = UserDatabaseManager(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (sharedPreferences.getBoolean("logged_in", false)) {
            returnToHome()
            return
        }

        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Background)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = findViewById<EditText>(R.id.Username)
        val password = findViewById<EditText>(R.id.Password)
        val loginButton = findViewById<Button>(R.id.LoginButton)
        val singUpButton = findViewById<Button>(R.id.SignupButton)

        loginButton.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            val found = database.verifyUser(usernameText, passwordText)

            if (found) {

                editor.putBoolean("logged_in", true)
                editor.putString("User", usernameText)
                editor.apply()

                returnToHome()
            }

            else {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show()
            }

        }

        singUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun returnToHome() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
    }
}