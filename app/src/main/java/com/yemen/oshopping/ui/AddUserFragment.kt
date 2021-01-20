package com.yemen.oshopping.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.MainScreen
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.splashScreen2
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class AddUserFragment : Fragment() {

    lateinit var fNameEditText: EditText
    lateinit var lNameEditText: EditText
    lateinit var addressEditText: EditText
    lateinit var phoneNumberEditText: EditText
    lateinit var detailsEditText: EditText
    lateinit var saveProfileBTN: Button
    lateinit var oshoppingViewModel: OshoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_user, container, false)
        fNameEditText = view.findViewById(R.id.first_name_edit_text)
        lNameEditText = view.findViewById(R.id.last_name_edit_text)
        addressEditText = view.findViewById(R.id.address_edit_text)
        phoneNumberEditText = view.findViewById(R.id.phone_edit_text)
        detailsEditText = view.findViewById(R.id.details_edit_text)
        saveProfileBTN = view.findViewById(R.id.save_profile_btn)
        val intent=Intent(requireContext(),MainScreen::class.java)
        saveProfileBTN.setOnClickListener {
            val user = User(
                first_name = fNameEditText.text.toString(),
                last_name = lNameEditText.text.toString(),
                email = oshoppingViewModel.getStoredEmail().toString(),
                address = addressEditText.text.toString(),
                phone_number = phoneNumberEditText.text.toString(),
                details = detailsEditText.text.toString()
            )
            oshoppingViewModel.apply {
                pushUser(user)


            }
            oshoppingViewModel.getUserByEmail(oshoppingViewModel.getStoredEmail().toString())
            oshoppingViewModel.userItemLiveDataByEmail.observe(viewLifecycleOwner, Observer { userdata ->
                userdata.get(0).user_id?.let { userId -> oshoppingViewModel.setUserId(userId) }

            })

            startActivity(intent)





        }



        return view
    }


    companion object {
        fun newInstance() =
            AddUserFragment()
    }
}