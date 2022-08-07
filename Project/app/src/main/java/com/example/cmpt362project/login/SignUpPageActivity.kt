package com.example.cmpt362project.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpPageActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var emailView: TextInputLayout
    private lateinit var usernameView: TextInputLayout
    private lateinit var passwordView: TextInputLayout
    private lateinit var confirmPasswordView: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temp1)

        emailView = findViewById(R.id.sign_up_email)
        usernameView = findViewById(R.id.sign_up_username)
        passwordView = findViewById(R.id.sign_up_password)
        confirmPasswordView = findViewById(R.id.sign_up_confirm_password)

        emailView.editText!!.addTextChangedListener { emailView.error = null }
        usernameView.editText!!.addTextChangedListener { usernameView.error = null }
        passwordView.editText!!.addTextChangedListener { passwordView.error = null }
        confirmPasswordView.editText!!.addTextChangedListener { confirmPasswordView.error = null }
        emailView.editText!!.setOnClickListener { emailView.error = null }
        usernameView.editText!!.setOnClickListener { usernameView.error = null }
        passwordView.editText!!.setOnClickListener { passwordView.error = null }
        confirmPasswordView.editText!!.setOnClickListener { confirmPasswordView.error = null }

        val toolbar = findViewById<Toolbar>(R.id.sign_up_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val confirmBtn = findViewById<Button>(R.id.sign_up_button)
        confirmBtn.setOnClickListener { signUp() }
    }

    // display error message;  if view is not null, set text color to error color
    fun showError(errorMsg: String, view: EditText?) {
        var er: TextView = findViewById<TextView>(R.id.error_message)
        er.text = errorMsg
        if (view != null) {
            view.setTextColor(resources.getColor(R.color.error))
        }
    }

    private fun signUp() {
        val username = findViewById<EditText>(R.id.sign_up_username)
        val password = findViewById<EditText>(R.id.sign_up_password)
        val email = findViewById<EditText>(R.id.sign_up_email)

        val usernameTxt = username.text.toString().toLowerCase()
        val passwordTxt = password.text.toString()
        val emailTxt = email.text.toString()

        // front-end input validation
        if (usernameTxt.isEmpty()) {
            showError("Error: Username is Empty", username)
            return
        } else if (passwordTxt.isEmpty()) {
            showError("Error: Password is Empty", password)
            return
        } else if (emailTxt.isEmpty()) {
            showError("Error: Email is Empty", email)
            return
        } else if (usernameTxt.length <= 3) {
            showError("Error: Username must be longer than 3 characters.", username)
            return
        } else if (passwordTxt.length < 6) {
            showError("Error: Password must be longer than 5 characters.", password)
            return
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            showError("Error: Invalid Email", email)
            return
        }

        // back-end input validation
        database = Firebase.database.reference
        auth = Firebase.auth

        // Referenced for ideas: https://stackoverflow.com/questions/35243492/firebase-android-make-username-unique
        database.child("usernames").child(usernameTxt).get().addOnSuccessListener {
            if (it.value != null) {
                showError("Error: Username Already Exists", username)
            } else {
                // add login data to auth
                auth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnSuccessListener(this) { task ->
                        // Sign up success
                        val user = auth.currentUser
                        val userData = User(usernameTxt, emailTxt, "", getString(R.string.default_pfp_path), "")

                        database.child("users").child(user!!.uid).setValue(userData)

                        // add unique username into database
                        database.child("usernames").child(usernameTxt).setValue(user.uid)
                        
                        // inform the user of success
                        Toast.makeText(this, "Account successfully created with username: ${usernameTxt}",
                            Toast.LENGTH_SHORT).show()

                        // go to login page
                        val intent = Intent(this, LoginPageActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener{
            showError("System Error: Firebase Error getting data", null)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        else -> {
            finish()
            true
        }
    }
}