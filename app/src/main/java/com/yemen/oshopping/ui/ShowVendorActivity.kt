package com.yemen.oshopping.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class ShowVendorActivity : AppCompatActivity() {
    lateinit var userName: TextView
    lateinit var userPhone: TextView
    lateinit var userDetails: TextView
    lateinit var userAddress: TextView
    lateinit var userEmail: TextView
    lateinit var user: User
    lateinit var chatImageBTN: ImageButton
    lateinit var reportImageBTN: ImageButton
    lateinit var callImageBTN: ImageButton
    lateinit var emailImageBTN: ImageButton
    private lateinit var oshoppingViewModel: OshoppingViewModel
    var vendorId=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_vendor)
        vendorId=intent.getIntExtra("VENDORID",1)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
            userName = findViewById(R.id.vendor_name_text_view)
            userEmail = findViewById(R.id.vendor_email_text_view)
            userPhone = findViewById(R.id.vendor_phone_text_view)
            userAddress = findViewById(R.id.vendor_address_text_view)
            userDetails = findViewById(R.id.vendor_details_text_view)
            reportImageBTN = findViewById(R.id.report_image_button)
            chatImageBTN = findViewById(R.id.chat_image_button)
            callImageBTN = findViewById(R.id.call_vendor_image_button)
            emailImageBTN = findViewById(R.id.email_vendor_image_button)
            reportImageBTN.setOnClickListener {
                ReportsDialog.newInstance().apply {
                    setTargetFragment(this, 0)
                    show(this.requireFragmentManager(), "Input")
                }
            }
            chatImageBTN.setOnClickListener {
                Toast.makeText(applicationContext, "chatImageBTN", Toast.LENGTH_SHORT).show()
            }
            callImageBTN.setOnClickListener {
                val callIntent = Intent().apply {
                    action = Intent.ACTION_CALL
                    data = Uri.parse("tel:${user.phone_number}")
                }
                if (callIntent.resolveActivity(packageManager) != null) {
                    startActivity(callIntent)
                }

            }

            emailImageBTN.setOnClickListener {
                val sendEmailIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(user.email))
                }
                if (sendEmailIntent.resolveActivity(packageManager) != null) {
                    startActivity(sendEmailIntent)
                }

            }

    }


    override fun onStart() {
        super.onStart()
        oshoppingViewModel.getUserById(vendorId)
        oshoppingViewModel.userLiveDataByID.observe(
            this,
            Observer { userDetails ->
                userDetails?.let {
                    Log.d("FromObserver", "$it")
                    this.user = userDetails
                    updateUI()
                }
            })
    }

    fun updateUI() {
        userName.text = user.first_name + " " + user.last_name
        userEmail.text = user.email
        userPhone.text = user.phone_number
        userAddress.text = user.address
        userDetails.text = user.details
    }
}

