package com.yemen.oshopping.setting


import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.vendor.UpdateProductFragmentArgs
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class UpdateUserFragment : Fragment() {

    val args: UpdateProductFragmentArgs by navArgs()
    private var selectedImageUri: Uri? = null
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
        editImageBTN=view.findViewById(R.id.edit_image_button)
        updateUserBtn=view.findViewById(R.id.updateUserBtn)
        userImage = view.findViewById(R.id.user_image)
        editImageBTN.setOnClickListener {
        }

        updateUserBtn.setOnClickListener {
            val user = User(
                first_name = fNameEditText.text.toString(),
                last_name = lNameEditText.text.toString(),
                email = oshoppingViewModel.getStoredEmail().toString(),
                address = addressEditText.text.toString(),
                phone_number = phoneNumberEditText.text.toString(),
                details = detailsEditText.text.toString(),
                image = imageName
            )
            oshoppingViewModel.updateUser(user)
        }
        return view
    }
}