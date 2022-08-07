package com.example.cmpt362project.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.example.cmpt362project.login.SignUpPageViewModel.SignUpPageErrors.*
import com.example.cmpt362project.login.SignUpPageViewModel.SignUpPageVMState.*
import com.example.cmpt362project.utility.FieldsLayoutUtility
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpPageActivity : AppCompatActivity() {
//    private lateinit var database: DatabaseReference
//    private lateinit var auth: FirebaseAuth

    private lateinit var emailView: TextInputLayout
    private lateinit var usernameView: TextInputLayout
    private lateinit var passwordView: TextInputLayout
    private lateinit var confirmPasswordView: TextInputLayout

    private lateinit var viewModel: SignUpPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temp1)

//        database = Firebase.database.reference
//        auth = Firebase.auth
        viewModel = ViewModelProvider(this)[SignUpPageViewModel::class.java]

        emailView = findViewById(R.id.sign_up_email)
        usernameView = findViewById(R.id.sign_up_username)
        passwordView = findViewById(R.id.sign_up_password)
        confirmPasswordView = findViewById(R.id.sign_up_confirm_password)

        FieldsLayoutUtility.setListenersForTextInputLayout(
            listOf(emailView, usernameView, passwordView, confirmPasswordView))

        val toolbar = findViewById<Toolbar>(R.id.sign_up_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val confirmBtn = findViewById<Button>(R.id.sign_up_button)
        confirmBtn.setOnClickListener {
            val email = emailView.editText!!.text.toString()
            val username = usernameView.editText!!.text.toString()
            val password = passwordView.editText!!.text.toString()
            val confirmPassword = confirmPasswordView.editText!!.text.toString()

            signUp2(email, username, password, confirmPassword)
        }
    }

    private fun signUp2(email: String, username: String, password: String, confirmPassword: String) {
        viewModel.signUp(email, username, password, confirmPassword).observe(this) { ret ->
            println("ret is $ret")
            when(ret) {
                SUCCESS -> {
                    Toast.makeText(this,
                        "Account successfully created with username: $username",
                        Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginPageActivity::class.java)
                    startActivity(intent)
                }
                FAILURE -> {
                    println("In SignUpPageViewModel.SignUpPageVMState.FAILURE")
                    val errors = viewModel.errorList.value
                    if (errors != null) {
                        println("Errors are $errors")
                        for (e in errors) {
                            when (e) {
                                EMAIL_EMPTY -> emailView.error = "E-mail field cannot be empty."
                                PASSWORD_EMPTY -> passwordView.error = "Password field cannot be empty."
                                USERNAME_SHORT -> usernameView.error = "Username must be longer than 3 characters."
                                PASSWORD_DIFFERENT -> confirmPasswordView.error = "Does not match the password field."
                                USERNAME_TAKEN -> usernameView.error = "This username is already taken."
                                PASSWORD_WEAK -> passwordView.error = "Password should be at least 6 characters."
                                EMAIL_MALFORMED -> emailView.error = "The email address is badly formatted."
                                EMAIL_TAKEN -> emailView.error = "The email address is already in use by another account."
                                UNKNOWN_ERROR -> Toast.makeText(baseContext, "System Error: Firebase Error getting data", Toast.LENGTH_SHORT).show()
                            }
                        }
                        viewModel.clearErrorList()
                    }
                }
                else -> {}
            }
        }
    }

//    private fun signUp(email: String, username: String, password: String, confirmPassword: String) {
//        var checkFields = true
//        if (email.isEmpty()) {
//            emailView.error = "E-mail field cannot be empty."
//            checkFields = false
//        }
//        if (password.isEmpty()) {
//            passwordView.error = "Password field cannot be empty."
//            checkFields = false
//        }
//        if (username.length <= 3) {
//            usernameView.error = "Username must be longer than 3 characters."
//            checkFields = false
//        }
//        if (password != confirmPassword) {
//            confirmPasswordView.error = "Does not match the password field."
//            checkFields = false
//        }
//        if (!checkFields) return
//
//        // Referenced for ideas: https://stackoverflow.com/questions/35243492/firebase-android-make-username-unique
//        database.child("usernames").child(username).get().addOnSuccessListener {
//            if (it.value != null) {
//                usernameView.error = "This username is already taken."
//            } else {
//                // add login data to auth
//                auth.createUserWithEmailAndPassword(email, password)
//                    .addOnSuccessListener(this) {
//                        // Sign up success
//                        val user = auth.currentUser
//                        val userData = User(username, email, "", getString(R.string.default_pfp_path), "")
//
//                        database.child("users").child(user!!.uid).setValue(userData)
//
//                        // add unique username into database
//                        database.child("usernames").child(username).setValue(user.uid)
//
//                        // inform the user of success
//                        Toast.makeText(this, "Account successfully created with username: $username",
//                            Toast.LENGTH_SHORT).show()
//
//                        // go to login page
//                        val intent = Intent(this, LoginPageActivity::class.java)
//                        startActivity(intent)
//                    }
//                    .addOnFailureListener { e ->
//                        when(e) {
//                            is FirebaseAuthWeakPasswordException ->
//                                passwordView.error = e.reason + "."
//                            else ->
//                                emailView.error = e.message
//                        }
//                    }
//            }
//        }.addOnFailureListener{
//            Toast.makeText(baseContext, "System Error: Firebase Error getting data", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        else -> {
            finish()
            true
        }
    }
}