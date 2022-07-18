package com.example.cmpt362project.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginPageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        title="Horizon Login"

        val loginBtn = findViewById<Button>(R.id.login_submit_btn)
        loginBtn.setOnClickListener(this)

        val email = findViewById<EditText>(R.id.login_email)
        val password = findViewById<EditText>(R.id.login_password)
        var error = findViewById<TextView>(R.id.error_message)


        email.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error.text = ""
                email.setTextColor(resources.getColor(R.color.black))
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
        val email = findViewById<EditText>(R.id.login_email)
        val password = findViewById<EditText>(R.id.login_password)

        val emailTxt = email.text.toString()
        val passwordTxt = password.text.toString()

        if (emailTxt.isEmpty()) {
            showError("Error: Username is Empty", email)
            return
        } else if (passwordTxt.isEmpty()) {
            showError("Error: Password is Empty", password)
            return
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            showError("Error: Invalid Email", email)
            return
        }

        // back-end input validation
        database = Firebase.database.reference
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(emailTxt, passwordTxt)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // success
                    database.child("users").child(user!!.uid).get().addOnSuccessListener {
                        if (it.value == null) {
                            // missing entire entry in database
                            showError("Error: Missing Entry", null)
                        } else {
                            val userData = it.value as Map<*, *>

                            Log.w("TAG", userData.toString())
                            Toast.makeText(
                                this, "Successfully logged in as: " +
                                        "${userData["username"]}", Toast.LENGTH_SHORT
                            ).show()
                            // move on to new activity
                            val bundle = Bundle()
                            bundle.putString("username", userData["username"].toString())
                            bundle.putString("email", userData["email"].toString())
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    showError("Error: Username or password is incorrect.", null)
                }
            }
    }
}