package com.example.cmpt362project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.cmpt362project.R
import com.example.cmpt362project.login.LoginPageActivity
import com.example.cmpt362project.login.SignUpPageActivity

class StartPageActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        val signupBtn = findViewById<Button>(R.id.login_redirect_btn)
        signupBtn.setOnClickListener(this)
        val loginBtn = findViewById<Button>(R.id.sign_up_redirect_btn)
        loginBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.login_redirect_btn) {
            val intent = Intent(this, LoginPageActivity::class.java)
            startActivity(intent)
        }

        else if (v.id == R.id.sign_up_redirect_btn) {
            val intent = Intent(this, SignUpPageActivity::class.java)
            startActivity(intent)
        }
    }
}