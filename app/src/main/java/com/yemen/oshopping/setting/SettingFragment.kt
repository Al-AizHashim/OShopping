package com.yemen.oshopping.setting

import android.app.PendingIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.yemen.oshopping.R
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class SettingFragment : Fragment() {
    lateinit var adminTV: TextView
    lateinit var contactUsTV: TextView
    lateinit var aboutUsTV: TextView
    lateinit var myProductTV: TextView
    lateinit var myAccountTV: TextView
    lateinit var signOutTV: TextView
    lateinit var signUpTV: TextView
    lateinit var chatTv: TextView
    lateinit var oshoppingViewModel: OshoppingViewModel
    //yemenoshopping@gmail.com
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }
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
        signUpTV=view.findViewById(R.id.sign_up)
        adminTV=view.findViewById(R.id.admin_page)
        chatTv=view.findViewById(R.id.chat)

        if (oshoppingViewModel.getStoredEmail().equals("yemenoshopping@gmail.com"))
        {
            adminTV.visibility = View.VISIBLE
            myProductTV.visibility = View.GONE
        }
        if(oshoppingViewModel.getStoredEmail().equals("none")){
            myProductTV.visibility = View.GONE
            adminTV.visibility = View.GONE
            signOutTV.visibility=View.GONE
            signUpTV.visibility=View.VISIBLE
        }

        myProductTV.setOnClickListener {
            if (oshoppingViewModel.getStoredEmail().equals("yemenoshopping@gmail.com"))
            {
                Toast.makeText(requireContext(), "You are an admin", Toast.LENGTH_SHORT).show()
            }
            else
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_myProductFragment)
        }
        adminTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_adminFragment)
        }
        myAccountTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_showUserFragment)
        }
        aboutUsTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_aboutUsFragment)
        }
        contactUsTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_contactUsFragment)
        }
        signOutTV.setOnClickListener {
            mAuth.signOut()
            oshoppingViewModel.apply {
                setUserId()
                setUserEmail()
                setQuery()
            }

            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_loginScreen)
           //write here the sign out code
        }
        signUpTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_signUp2)
        }

        chatTv.setOnClickListener {
            if(oshoppingViewModel.getStoredUserId()==-1) {
                Toast.makeText(requireContext(), "You must create an account", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_signUp2)
            }
            else{
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_usersActivity)
            }
        }


    return view
    }


}