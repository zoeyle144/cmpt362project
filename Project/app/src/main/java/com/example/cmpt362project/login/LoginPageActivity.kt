package com.example.cmpt362project.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginPageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        title="Horizon Login"

        val loginBtn = findViewById<Button>(R.id.login_submit_btn)
        loginBtn.setOnClickListener(this)

        val username = findViewById<EditText>(R.id.login_username)
        val password = findViewById<EditText>(R.id.login_password)
        var error = findViewById<TextView>(R.id.error_message)


        username.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error.text = ""
                username.setTextColor(resources.getColor(R.color.black))
            }
        })

        password.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error.text = ""
                password.setTextColor(resources.getColor(R.color.black))
            }
        })
    }


    // display error message;  if view is not null, set text color to error color
    fun showError(errorMsg: String, view: EditText?) {
        var er: TextView = findViewById<TextView>(R.id.error_message)
        er.text = errorMsg
        if (view != null) {
            view.setTextColor(resources.getColor(R.color.error))
        }
    }

    override fun onClick(v: View) {
        val username = findViewById<EditText>(R.id.login_username)
        val password = findViewById<EditText>(R.id.login_password)

        val usernameTxt = username.text.toString()
        val passwordTxt = password.text.toString()

        if (usernameTxt.isEmpty()) {
            showError("Error: Username is Empty", username)
            return
        } else if (passwordTxt.isEmpty()) {
            showError("Error: Password is Empty", password)
            return
        }

        // back-end input validation
        database = Firebase.database.reference
        database.child("users").child(usernameTxt).get().addOnSuccessListener {
            if (it.value == null) {
                // missing entire entry in database
                showError("Error: Username or password is incorrect.", null)
            } else {
                val user = it.value as Map<*, *>
                if (user["username"] == null || user["password"] == null) {
                    // missing parts in database
                    showError("Error: Username or password is incorrect.", null)
                } else {
                    if (user["username"] == usernameTxt &&
                        user["password"] == passwordTxt) {
                        // success
                        Toast.makeText(this, "Successfully logged in as: ${user.get("username")}",
                            Toast.LENGTH_SHORT).show()

                        // move on to new activity
                        val bundle = Bundle()
                        bundle.putString("username", user["username"].toString())
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else {
                        showError("Error: Username or password is incorrect.", null)
                    }
                }
            }

        }.addOnFailureListener{
            showError("System Error: Firebase Error getting data", null)
        }
    }
}