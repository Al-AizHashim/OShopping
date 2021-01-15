package com.yemen.oshopping.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yemen.oshopping.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

/*
        adminTV.setOnClickListener {
            launchFragment(AdminFragment())
        }

        aboutUsTV.setOnClickListener {
            launchFragment(AboutUsFragment())
        }
        myAccountTV.setOnClickListener {
            launchFragment(MyAccountFragment())
        }
        contactUsTV.setOnClickListener {
            //launchFragment()
        }
        myProductV.setOnClickListener {

           launchFragment(MyProductFragment())
        }

 */

    }

/*
    fun launchFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction().attach( fragment)
            .addToBackStack(null)
            .commit()

    }

 */
}