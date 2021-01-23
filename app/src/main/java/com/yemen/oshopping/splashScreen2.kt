package com.yemen.oshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class splashScreen2 : AppCompatActivity() {

    private lateinit var skip: Button
    private lateinit var login: Button
    private lateinit var oshoppingViewModel: OshoppingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        skip = findViewById(R.id.skip)
        login = findViewById(R.id.log_in)
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        skip.setOnClickListener {
            oshoppingViewModel.setUserId(-1)
            var intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            var intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
    }

}