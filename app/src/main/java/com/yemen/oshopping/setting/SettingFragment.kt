package com.yemen.oshopping.setting

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.yemen.oshopping.R
import com.yemen.oshopping.SignUp
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlin.concurrent.thread


class SettingFragment : Fragment() {
    lateinit var adminTV: CardView
    lateinit var contactUsTV: CardView
    lateinit var aboutUsTV: CardView
    lateinit var myProductTV: CardView
    lateinit var myAccountTV: CardView
    lateinit var signOutTV: Button
    lateinit var signUpTV: Button
    lateinit var chatTv: CardView
    lateinit var close: ImageButton
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
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        myAccountTV = view.findViewById(R.id.my_account)
        myProductTV = view.findViewById(R.id.my_products)
        aboutUsTV = view.findViewById(R.id.about_us)
        contactUsTV = view.findViewById(R.id.contact_us)
        signOutTV = view.findViewById(R.id.sign_out)
        signUpTV = view.findViewById(R.id.sign_up)
        adminTV = view.findViewById(R.id.admin_page)
        chatTv = view.findViewById(R.id.chat)
        close = view.findViewById(R.id.bt_close)

        if (oshoppingViewModel.getStoredEmail().equals("yemenoshopping@gmail.com")) {
            adminTV.visibility = View.VISIBLE
            // myProductTV.visibility = View.GONE
        }
        if (oshoppingViewModel.getStoredEmail().equals("none")) {
            //myProductTV.visibility = View.GONE
            adminTV.visibility = View.GONE
            signOutTV.visibility = View.GONE
            signUpTV.visibility = View.VISIBLE
        }

        myProductTV.setOnClickListener {


            if (oshoppingViewModel.getStoredEmail().equals("yemenoshopping@gmail.com")) {
                Toast.makeText(requireContext(), "You are an admin", Toast.LENGTH_SHORT).show()
            } else if (oshoppingViewModel.getStoredEmail().equals("none")) {
                toastIconError("You should create an account")
            } else {



                if (oshoppingViewModel.getStoredUserBlock()==1){
                    toastIconError2("YOU ARE BLOCKED")
                }
                else {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_settingFragment_to_myProductFragment)
                }
            }
        }
        adminTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_settingFragment_to_adminFragment)
        }
        myAccountTV.setOnClickListener {
            if (oshoppingViewModel.getStoredEmail().equals("none")) {
                toastIconError("You should create an account")
            } else {
                Navigation.findNavController(view)
                    .navigate(R.id.action_settingFragment_to_showUserFragment)
            }
        }
        aboutUsTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_settingFragment_to_aboutUsFragment)
        }
        contactUsTV.setOnClickListener {
            showContactUsDialog()
            // Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_contactUsFragment)
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
            /*    if(oshoppingViewModel.getStoredUserId()==-1) {
                    Toast.makeText(requireContext(), "You must create an account", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_signUp2)
                }
                else{
                    Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_usersActivity)
                }*/
            if (oshoppingViewModel.getStoredEmail().equals("none")) {
                toastIconError("You should create an account")
            } else {
                Navigation.findNavController(view)
                    .navigate(R.id.action_settingFragment_to_usersActivity)
            }
        }
        close.setOnClickListener {
            activity?.onBackPressed()
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        oshoppingViewModel.apply { getUserById(getStoredUserId()) }
        oshoppingViewModel.userLiveDataByID.observe(
            requireActivity(),
            Observer { userdata ->
                oshoppingViewModel.setUserBlock(userdata.block)


            })
    }

    private fun showContactUsDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_contact_project)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.findViewById<View>(R.id.bt_close).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.email_fab).setOnClickListener {
            sendEmail()
        }
        dialog.findViewById<View>(R.id.call_fab).setOnClickListener {
            callme()

        }
        dialog.show()
    }

    fun sendEmail() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("alaiz.hashim@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Email subject")
            putExtra(Intent.EXTRA_TEXT, "Email message text")
        }
        if (sendIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(sendIntent)
        }
    }


    private fun callme() {
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:+967775301780")

        }
        if (callIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(callIntent)
        }
    }

    fun addContact() {
        val addContactIntent = Intent().apply {
            action = Intent.ACTION_INSERT
            setType(ContactsContract.Contacts.CONTENT_TYPE)
            putExtra(ContactsContract.Intents.Insert.NAME, "Al-Aiz Hashim")
            putExtra(ContactsContract.Intents.Insert.PHONE, "tel:+967775301780")
            putExtra(ContactsContract.Intents.Insert.EMAIL, "alaiz.hashim@gmail.com")

        }
        if (addContactIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(addContactIntent)
        }
    }

    private fun toastIconError(message:String) {
        val toast = Toast(activity?.applicationContext)
        toast.duration = Toast.LENGTH_LONG

        //inflate view
        val custom_view =
            layoutInflater.inflate(R.layout.toast_icon_text, null)
        (custom_view.findViewById<View>(R.id.message) as TextView).text =message

        (custom_view.findViewById<View>(R.id.icon) as ImageView).setImageResource(
            R.drawable.ic_close
        )
        (custom_view.findViewById<View>(R.id.parent_view) as CardView).setCardBackgroundColor(
            resources.getColor(R.color.red)
        )
        toast.view = custom_view
        toast.show()
        view?.let {

            Navigation.findNavController(it).navigate(R.id.action_settingFragment_to_signUp2)
        }
    }

    private fun toastIconError2(message:String) {
        val toast = Toast(activity?.applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.BOTTOM,0,200)

        //inflate view
        val custom_view =
            layoutInflater.inflate(R.layout.toast_icon_text, null)
        (custom_view.findViewById<View>(R.id.message) as TextView).text =message

        (custom_view.findViewById<View>(R.id.icon) as ImageView).setImageResource(
            R.drawable.ic_close
        )
        (custom_view.findViewById<View>(R.id.parent_view) as CardView).setCardBackgroundColor(
            resources.getColor(R.color.red)
        )
        toast.view = custom_view
        toast.show()
    }
}