package com.yemen.oshopping.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class ShowUserFragment : Fragment() {
    lateinit var userName:TextView
    lateinit var userPhone:TextView
    lateinit var userDetails:TextView
    lateinit var userAddress:TextView
    lateinit var userEmail:TextView
    lateinit var editImageBTN:ImageButton
    lateinit var chatImageBTN:ImageButton
    lateinit var user: User
    private lateinit var oshoppingViewModel: OshoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getUserById(oshoppingViewModel.getStoredUserId())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var view =inflater.inflate(R.layout.fragment_show_user, container, false)
        userName=view.findViewById(R.id.user_name_text_view)
        userEmail=view.findViewById(R.id.user_email_text_view)
        userPhone=view.findViewById(R.id.user_phone_text_view)
        userAddress=view.findViewById(R.id.user_address_text_view)
        userDetails=view.findViewById(R.id.user_details_text_view)
        editImageBTN=view.findViewById(R.id.edit_image_button)
        chatImageBTN=view.findViewById(R.id.chat_image_button)
        editImageBTN.setOnClickListener {
            val action=
                user.user_id?.let { it1 ->
                    ShowUserFragmentDirections.actionShowUserFragmentToUpdateUserFragment2(userId = it1,firstName =user.first_name,
                        lastName = user.last_name,email = user.email,phoneNumber = user.phone_number,
                        image = user.image,details =  user.details,address=user.address)

                }
                if (action != null) {
                    Navigation.findNavController(view)
                        .navigate(action)
                }
            }

        chatImageBTN.setOnClickListener {

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
            ShowUserFragment()
    }
}
