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


        var LOCAL_HOST_URI="http://10.0.2.2/oshopping_api/"
        var BASE_LOCAL_HOST_URI="http://10.0.2.2/"


    }


}