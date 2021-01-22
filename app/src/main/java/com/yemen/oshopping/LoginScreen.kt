package com.yemen.oshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.yemen.oshopping.Chat.activity.UsersActivity
import com.yemen.oshopping.sharedPreferences.SharedPreference
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginScreen : AppCompatActivity() , View.OnClickListener{
    private val TAG = "FirebaseEmailPassword"
    private lateinit var oShoppingViewModel:OshoppingViewModel
    private lateinit var skip: TextView
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oShoppingViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        setContentView(R.layout.activity_login_screen)
        val sharedPreference: SharedPreference =SharedPreference(this)
        skip = findViewById(R.id.skip_text_view)
        supportActionBar?.hide()
        login_button.setOnClickListener(this)
        signUp.setOnClickListener(this)
        forgot_password.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()

        skip.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }
        rememberMe.setOnCheckedChangeListener{buttonView, isChecked ->
            if(isChecked)
            {
               // Toast.makeText(this, mAuth!!.currentUser.toString(),Toast.LENGTH_LONG).show()
                sharedPreference.save("rememberMe",true)
              //  sharedPreference.save("email",mAuth.currentUser.)
            }
            else
            {
                sharedPreference.save("rememberMe",false)
            }
        }

    }

    override fun onClick(v: View?) {
        val i = v!!.id

        if (i == R.id.signUp) {

            val intent = Intent(this, SignUp::class.java)

            startActivity(intent)


        } else if (i == R.id.login_button) {

            signIn(email.text.toString(), password.text.toString())

        }
        else if(i==R.id.forgot_password)
        {
            val intent = Intent(this, ResetPassword::class.java)

            startActivity(intent)
        }
    }


    private fun signIn(email: String, password: String) {
        val sharedPreference: SharedPreference =SharedPreference(this)
        Log.e(TAG, "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "signIn: Success!")
                    if(email=="yemenoshopping@gmail.com ")
                        sharedPreference.save("userType","Admin")
                    else
                        sharedPreference.save("userType","Customer")
                    sharedPreference.save("userEmail",email)
                    oShoppingViewModel.setUserEmail(email)
                    //val user = mAuth!!.currentUser
           // val intent = Intent(this, MainScreen::class.java)
                    val intent = Intent(this, UsersActivity::class.java)

                    startActivity(intent)


                } else {
                    Log.e(TAG, "signIn: Fail!", task.exception)
                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()

                }

                if (!task.isSuccessful) {
                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()

                }
            }
    }
    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}