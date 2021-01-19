package com.yemen.oshopping.sharedPreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.R
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class TestActivity : AppCompatActivity() {
    lateinit var oViewModel:OshoppingViewModel
    lateinit var showbtn:Button
    lateinit var savebtn:Button
    lateinit var showEmailTextView: TextView
    lateinit var storeEmail:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        oViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        showbtn=findViewById(R.id.show_shared_email)
        savebtn=findViewById(R.id.store_email)
        showEmailTextView=findViewById(R.id.email_tv)
        storeEmail=findViewById(R.id.email_et)
        showEmailTextView.setText(oViewModel.getStoredEmail())
        showbtn.setOnClickListener {
            showEmailTextView.setText(oViewModel.getStoredEmail())
        }
        savebtn.setOnClickListener {
            oViewModel.setUserEmail(storeEmail.text.toString())
        }
    }
}