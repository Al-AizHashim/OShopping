package com.yemen.oshopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yemen.oshopping.admin.AddCategoryFragment

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

}

    companion object{

        var LOCAL_HOST_URI="http://192.168.1.3/oshopping_api/"

    }


}