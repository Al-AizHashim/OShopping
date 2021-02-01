package com.yemen.oshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yemen.oshopping.Chat.adapter.UserAdapter
import com.yemen.oshopping.Chat.model.User
import com.yemen.oshopping.sharedPreferences.SharedPreference
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.user_activity.*

class LoginScreen : AppCompatActivity() , View.OnClickListener{
    private val TAG = "FirebaseEmailPassword"
    private lateinit var oShoppingViewModel:OshoppingViewModel

    private var mAuth: FirebaseAuth? = null
   private lateinit var userList:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oShoppingViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        setContentView(R.layout.activity_login_screen)
        val sharedPreference: SharedPreference =SharedPreference(this)

        supportActionBar?.hide()
        login_button.setOnClickListener(this)
        signUp.setOnClickListener(this)
        forgot_password.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
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
            oShoppingViewModel.setUserEmail(email.text.toString())
            oShoppingViewModel.getUserByEmail(email.text.toString())

            oShoppingViewModel.userItemLiveDataByEmail.observe(
                this,
                Observer { userdata ->
                userdata.get(0).user_id?.let { userId -> oShoppingViewModel.setUserId(userId)
                userdata.get(0).block?.let { oShoppingViewModel.setUserBlock(it) }
                }

            })

        }
        else if(i==R.id.forgot_password)
        {
            val intent = Intent(this, ResetPassword::class.java)

            startActivity(intent)
        }
    }


    private fun signIn(emaill: String, passwordd: String) {
        val sharedPreference: SharedPreference =SharedPreference(this)
        Log.e(TAG, "signIn:" + emaill)
        if (!validateForm(emaill, passwordd)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(emaill, passwordd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "signIn: Success!")
                    if(emaill=="yemenoshopping@gmail.com ")
                        sharedPreference.save("userType","Admin")
                    else
                        sharedPreference.save("userType","Customer")
                    sharedPreference.save("userEmail",emaill)

                    var userid =   mAuth!!.uid

                    userid?.let { sharedPreference.save("userId", it) }
                    Log.e(TAG, "USERID: ${userid}!")
                    //val user = mAuth!!.currentUser
           // val intent = Intent(this, MainScreen::class.java)

                    val databaseReference: DatabaseReference =
                        FirebaseDatabase.getInstance().getReference("Users")


                    databaseReference.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {

                            val currentUser = snapshot.getValue(User::class.java)
                            currentUser?.userName?.let { sharedPreference.save("userName", it) }
                            Log.e(TAG, "current user name ${currentUser?.userName}")

                        }

                    })
                            val intent = Intent(this, MainScreen::class.java)

                    startActivity(intent)


                } else {
                    email.error="Email or Password is wrong"
                    password.error="Email or Password is wrong"
                   /* Log.e(TAG, "signIn: Fail!", task.exception)
                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
*/
                }

                if (!task.isSuccessful) {
                    email.error="Email or Password is wrong"
                    password.error="Email or Password is wrong"
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