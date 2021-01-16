package com.yemen.oshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class splashScreen2 : AppCompatActivity() {

    private lateinit var skip: Button
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        skip = findViewById(R.id.skip)
        login = findViewById(R.id.log_in)
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        skip.setOnClickListener {
            var intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            var intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
    }

}