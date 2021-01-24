package com.yemen.oshopping.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class ShowVendorFragment : Fragment() {
    lateinit var userName:TextView
    lateinit var userPhone:TextView
    lateinit var userDetails:TextView
    lateinit var userAddress:TextView
    lateinit var userEmail:TextView
    lateinit var user: User
    private var param1: Int = 1
    lateinit var chatImageBTN:ImageButton
    lateinit var reportImageBTN:ImageButton
    lateinit var callImageBTN:ImageButton
    lateinit var emailImageBTN:ImageButton
    private lateinit var oshoppingViewModel: OshoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.apply {
            getUserById(getStoredUserId())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var view =inflater.inflate(R.layout.fragment_vendor_profile, container, false)
        userName=view.findViewById(R.id.vendor_name_text_view)
        userEmail=view.findViewById(R.id.vendor_email_text_view)
        userPhone=view.findViewById(R.id.vendor_phone_text_view)
        userAddress=view.findViewById(R.id.vendor_address_text_view)
        userDetails=view.findViewById(R.id.vendor_details_text_view)
        reportImageBTN=view.findViewById(R.id.report_image_button)
        chatImageBTN=view.findViewById(R.id.chat_image_button)
        callImageBTN=view.findViewById(R.id.call_vendor_image_button)
        emailImageBTN=view.findViewById(R.id.email_vendor_image_button)
        reportImageBTN.setOnClickListener {
            ReportsDialog.newInstance().apply {
                setTargetFragment(this@ShowVendorFragment, 0)
                show(this@ShowVendorFragment.requireFragmentManager(), "Input")
            }
        }
        chatImageBTN.setOnClickListener {
            Toast.makeText(this@ShowVendorFragment.context, "chatImageBTN", Toast.LENGTH_SHORT).show()
        }
        callImageBTN.setOnClickListener {
            val callIntent= Intent().apply{
                action=Intent.ACTION_CALL
                data= Uri.parse("tel:${user.phone_number}")
            }
            if(callIntent.resolveActivity(activity?.packageManager) !=null){
                startActivity(callIntent)
            }

        }

        emailImageBTN.setOnClickListener {
            val sendEmailIntent= Intent().apply{
                action=Intent.ACTION_SEND
                type="text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(user.email))
            }
            if(sendEmailIntent.resolveActivity(activity?.packageManager) !=null){
                startActivity(sendEmailIntent)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.userLiveDataByID.observe(
            viewLifecycleOwner,
            Observer { userDetails ->
                userDetails?.let {
                    Log.d("FromObserver", "$it")
                    this.user = userDetails
                    updateUI()
                }
            })
    }

    fun updateUI() {
        userName.text = user.first_name+" "+user.last_name
        userEmail.text = user.email
        userPhone.text = user.phone_number
        userAddress.text= user.address
        userDetails.text = user.details
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShowVendorFragment()
    }
}
