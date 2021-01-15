package com.yemen.oshopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yemen.oshopping.setting.AboutUsFragment
import com.yemen.oshopping.setting.AdminFragment
import com.yemen.oshopping.setting.MyAccountFragment
import com.yemen.oshopping.setting.MyProductFragment

class SettingActivity : AppCompatActivity() {
    lateinit var adminTV: TextView
    lateinit var contactUsTV: TextView
    lateinit var aboutUsTV: TextView
    lateinit var myProductV: TextView
    lateinit var myAccountTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        myAccountTV=findViewById(R.id.my_account)
        myProductV=findViewById(R.id.my_products)
        aboutUsTV=findViewById(R.id.about_us)
        contactUsTV=findViewById(R.id.contact_us)
        adminTV=findViewById(R.id.admin_page)

        adminTV.visibility = View.GONE

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

    }


    fun launchFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_container, fragment)
            .addToBackStack(null)
            .commit()

    }
}