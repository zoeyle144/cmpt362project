package com.example.cmpt362project.login

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.login.LoginPageViewModel.LoginPageErrors.*
import com.example.cmpt362project.login.LoginPageViewModel.LoginPageVMState.FAILURE
import com.example.cmpt362project.login.LoginPageViewModel.LoginPageVMState.SUCCESS
import com.example.cmpt362project.utility.FieldsLayoutUtility
import com.google.android.material.textfield.TextInputLayout

class LoginPageActivity : AppCompatActivity() {
    private lateinit var emailView: TextInputLayout
    private lateinit var passwordView: TextInputLayout

    private lateinit var viewModel: LoginPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temp2)

        viewModel = ViewModelProvider(this)[LoginPageViewModel::class.java]

        emailView = findViewById(R.id.login_email)
        passwordView = findViewById(R.id.login_password)

        FieldsLayoutUtility.setListenersForTextInputLayout(listOf(emailView, passwordView))

        val toolbar = findViewById<Toolbar>(R.id.login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val email = emailView.editText!!.text.toString()
            val password = passwordView.editText!!.text.toString()

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(this) {
            when (it) {
                SUCCESS -> startMainActivity()
                FAILURE -> handleLoginErrors()
                else -> {}
            }
        }
    }

    private fun startMainActivity() {
        val bundle = Bundle()
        val data = viewModel.returnVal

        Toast.makeText(this, "Logged in as: ${data.first}", Toast.LENGTH_SHORT).show()
        bundle.putString("username", data.first)
        bundle.putString("email", data.second)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun handleLoginErrors() {
        val errors = viewModel.errorList.value
        if (errors != null) {
            for (e in errors) {
                when (e) {
                    EMAIL_EMPTY -> emailView.error = "E-mail field cannot be empty."
                    PASSWORD_EMPTY -> passwordView.error = "Password field cannot be empty."
                    NO_USER_ID -> Toast.makeText(
                        baseContext,
                        "Error: user doesn't have ID in the Realtime Database", Toast.LENGTH_SHORT
                    ).show()
                    WRONG_CREDENTIALS -> {
                        emailView.error = "E-mail may be incorrect."
                        passwordView.error = "Password may be incorrect."
                    }
                }
            }
            viewModel.clearErrorList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        else -> {
            finish()
            true
        }
    }
}
