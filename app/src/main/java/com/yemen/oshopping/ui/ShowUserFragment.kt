package com.yemen.oshopping.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.model.User
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.*

class ShowUserFragment : Fragment() {
    lateinit var userName:TextView
    lateinit var userPhone:TextView
    lateinit var userDetails:TextView
    lateinit var userAddress:TextView
    lateinit var userEmail:TextView
    lateinit var user: User
    private var param1: Int = 1
    private lateinit var oshoppingViewModel: OshoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getUserById(param1)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var view =inflater.inflate(R.layout.fragment_show_user, container, false)
        userName=view.findViewById(R.id.user_name)
        userEmail=view.findViewById(R.id.user_email)
        userPhone=view.findViewById(R.id.user_phone)
        userAddress=view.findViewById(R.id.user_address)
        userDetails=view.findViewById(R.id.user_details)
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
        userName.text = user.first_name+user.last_name
        userEmail.text = user.email
        userPhone.text = user.phone_number
        userAddress.text= user.address
        userDetails.text = user.details
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShowUserFragment()
    }
}
