package com.example.cmpt362project.login

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.login.SignUpPageViewModel.SignUpPageErrors.*
import com.example.cmpt362project.login.SignUpPageViewModel.SignUpPageVMState.FAILURE
import com.example.cmpt362project.login.SignUpPageViewModel.SignUpPageVMState.SUCCESS
import com.example.cmpt362project.utility.FieldsLayoutUtility
import com.google.android.material.textfield.TextInputLayout

class SignUpPageActivity : AppCompatActivity() {
    private lateinit var emailView: TextInputLayout
    private lateinit var usernameView: TextInputLayout
    private lateinit var passwordView: TextInputLayout
    private lateinit var confirmPasswordView: TextInputLayout

    private lateinit var viewModel: SignUpPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temp1)

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

            signUp(email, username, password, confirmPassword)
        }
    }

    private fun signUp(email: String, username: String, password: String, confirmPassword: String) {
        viewModel.signUp(email, username, password, confirmPassword).observe(this) { ret ->
            when(ret) {
                SUCCESS -> startLoginActivity(username)
                FAILURE -> handleSignUpErrors()
                else -> {}
            }
        }
    }

    private fun handleSignUpErrors() {
        val errors = viewModel.errorList.value
        if (errors != null) {
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

    private fun startLoginActivity(username: String) {
        Toast.makeText(this, "Account successfully created with username: $username",
            Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        else -> {
            finish()
            true
        }
    }
}