package com.yemen.oshopping.setting


import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.sharedPreferences.UserSharedPreferences
import com.yemen.oshopping.vendor.UpdateProductFragmentArgs
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class UpdateUserFragment : Fragment() {

  val uargs: UpdateUserFragmentArgs by navArgs()
    private var uri: String? = null
    var imageName:String?=null
    lateinit var fNameEditText: EditText
    lateinit var lNameEditText: EditText
    lateinit var addressEditText: EditText
    lateinit var phoneNumberEditText: EditText
    lateinit var detailsEditText: EditText
    lateinit var editImageBTN: ImageButton
    lateinit var updateUserBtn:Button
    lateinit var userImage: ImageView
    lateinit var user: User
    private lateinit var oshoppingViewModel: OshoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_user, container, false)
        fNameEditText = view.findViewById(R.id.first_name_edit_text)
        lNameEditText = view.findViewById(R.id.last_name_edit_text)
        addressEditText = view.findViewById(R.id.address_edit_text)
        phoneNumberEditText = view.findViewById(R.id.phone_edit_text)
        detailsEditText = view.findViewById(R.id.details_edit_text)
        updateUserBtn=view.findViewById(R.id.updateUserBtn)

        userImage = view.findViewById(R.id.user_image)
        fNameEditText.setText(uargs.firstName.toString())
        lNameEditText.setText(uargs.lastName.toString())
        addressEditText.setText(uargs.address)
        phoneNumberEditText.setText(uargs.phoneNumber)
        detailsEditText.setText(uargs.details)
        Picasso.get().load(uri+uargs.email).into(userImage)


        updateUserBtn.setOnClickListener {
            val user = User(
                user_id = oshoppingViewModel.getStoredUserId(),
                first_name = fNameEditText.text.toString(),
                last_name = lNameEditText.text.toString(),
                phone_number = phoneNumberEditText.text.toString(),
                address = addressEditText.text.toString(),
                details = detailsEditText.text.toString()
//                ,image = imageName
            )
            oshoppingViewModel.updateUser(user)
            Navigation.findNavController(view)
                .navigate(R.id.action_updateUserFragment_to_showUserFragment)
        }
        return view
    }
}