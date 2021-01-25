package com.yemen.oshopping.setting


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.vendor.UpdateProductFragmentArgs
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class UpdateUserFragment : Fragment() {

    val args: UpdateProductFragmentArgs by navArgs()
    lateinit var userName:TextView
    lateinit var userPhone:TextView
    lateinit var userDetails:TextView
    lateinit var userAddress:TextView
    lateinit var userEmail:TextView
    lateinit var editImageBTN: ImageButton
    lateinit var updateUserBtn:Button
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
        userName=view.findViewById(R.id.user_name_text_view)
        userEmail=view.findViewById(R.id.user_email_text_view)
        userPhone=view.findViewById(R.id.user_phone_text_view)
        userAddress=view.findViewById(R.id.user_address_text_view)
        userDetails=view.findViewById(R.id.user_details_text_view)
        editImageBTN=view.findViewById(R.id.edit_image_button)
        updateUserBtn=view.findViewById(R.id.updateUserBtn)
        editImageBTN.setOnClickListener {
        }

        updateUserBtn.setOnClickListener {
            val user = User(
                user_id = args.productId,
                first_name = userName.text.toString(),
                last_name = userName.text.toString(),
                phone_number  = userPhone.text.toString(),
                details = userDetails.text.toString(),
                address = userAddress.text.toString())
            oshoppingViewModel.updateUser(user)
        }
        return view
    }
}