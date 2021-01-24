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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yemen.oshopping.ui.AddUserFragment
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.GoogleAccount
import kotlinx.android.synthetic.main.activity_sign_up.emailUse


class SignUp : AppCompatActivity(), View.OnClickListener{
    private lateinit var oShoppingViewModel: OshoppingViewModel
    private lateinit var skip: TextView
    private val TAG = "FirebaseEmailPassword"
    val RC_SIGN_IN: Int = 1
    lateinit var signInClient: GoogleSignInClient
    lateinit var signInOptions: GoogleSignInOptions
    private var mAuth: FirebaseAuth? = null
    private lateinit var databaseReference:DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        oShoppingViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        skip = findViewById(R.id.skip_text_view)
        supportActionBar?.hide()
        initializeUI()
        setupGoogleLogin()
        emailUse.setOnClickListener(this)
        GoogleAccount.setOnClickListener (this )
        logIn.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()

        skip.setOnClickListener {
            var intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }

    }


    private fun createAccount(email: String, password: String) {
        Log.e(TAG, "createAccount:" + email)

        print("inisde sign up")
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "createAccount: Success!")
                    oShoppingViewModel.setUserEmail(email)

                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
                    var user :FirebaseUser?= mAuth!!.currentUser
                    var userId:String=user!!.uid
                    databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    var hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",name.text.toString())
                    hashMap.put("userEmail",email)
                    hashMap.put("profileImage","")
                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                      if(it.isSuccessful)
                      {
                          val fragment = AddUserFragment()
                          supportFragmentManager
                              .beginTransaction()
                              .replace(R.id.sign_up_container, fragment)
                              .addToBackStack(null)
                              .commit()
                         // val intent = Intent(this, LoginScreen::class.java)
                          //startActivity(intent)
                      }
                    }
                } else {
                    Log.e(TAG, "createAccount: Fail!", task.exception)
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

    private fun login() {
        val loginIntent: Intent = signInClient.signInIntent
        startActivityForResult(loginIntent, RC_SIGN_IN)

    }
    private fun initializeUI() {
        GoogleAccount.setOnClickListener {
            login()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    googleFirebaseAuth(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun googleFirebaseAuth(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val fragment = AddUserFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.sign_up_container, fragment)
                    .addToBackStack(null)
                    .commit()

                //val intent = Intent(this, LoginScreen::class.java)

               // startActivity(intent)
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupGoogleLogin() {
      /*  signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(this, signInOptions)*/
    }

    override fun onClick(v: View?) {
        var i=v!!.id
        if(i==R.id.emailUse)
        {
            createAccount(emailSignUp.text.toString(), passwordSignUp.text.toString())
        }
        else if(i==R.id.GoogleAccount)
        {
            login()
        }
        else if(i==R.id.logIn)
        {
            val intent = Intent(this, LoginScreen::class.java)

            startActivity(intent)
        }
    }

}