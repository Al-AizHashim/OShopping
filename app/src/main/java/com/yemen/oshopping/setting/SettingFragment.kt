package com.yemen.oshopping.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.yemen.oshopping.R


class SettingFragment : Fragment() {
    lateinit var adminTV: TextView
    lateinit var contactUsTV: TextView
    lateinit var aboutUsTV: TextView
    lateinit var myProductTV: TextView
    lateinit var myAccountTV: TextView
    lateinit var signOutTV: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setting, container, false)
        myAccountTV=view.findViewById(R.id.my_account)
        myProductTV=view.findViewById(R.id.my_products)
        aboutUsTV=view.findViewById(R.id.about_us)
        contactUsTV=view.findViewById(R.id.contact_us)
        signOutTV=view.findViewById(R.id.sign_out)
        adminTV=view.findViewById(R.id.admin_page)
        adminTV.visibility = View.VISIBLE

        myProductTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_myProductFragment)
        }
        adminTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_adminFragment)
        }
        myAccountTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_myAccountFragment)
        }
        aboutUsTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_aboutUsFragment)
        }
        contactUsTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_contactUsFragment)
        }
        signOutTV.setOnClickListener {
           //write here the sign out code

        }


    return view
    }


}