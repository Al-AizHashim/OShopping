package com.yemen.oshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.yemen.oshopping.sharedPreferences.SharedPreference

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private  var rememberMe:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference: SharedPreference = SharedPreference(this)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        Handler().postDelayed({

            rememberMe=sharedPreference.getValueBoolean("rememberMe",false)
            if (rememberMe) {
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
                finish()
            }
        else {    val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()}
        }, 3000)
    }
}