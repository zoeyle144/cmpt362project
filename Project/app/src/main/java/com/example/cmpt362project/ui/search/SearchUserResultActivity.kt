package com.example.cmpt362project.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.cmpt362project.R

class SearchUserResultActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH_USER_RESULT_USERNAME = "KEY_SEARCH_USER_RESULT_USERNAME"
        const val KEY_SEARCH_USER_RESULT_EMAIL = "KEY_SEARCH_USER_RESULT_EMAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user_result)

        val usernameView = findViewById<TextView>(R.id.search_user_result_username)
        val emailView = findViewById<TextView>(R.id.search_user_result_email)

        var username = ""
        var email = ""

        val extras = intent.extras
        if (extras != null) {
            username = extras.getString(KEY_SEARCH_USER_RESULT_USERNAME, "")
            email = extras.getString(KEY_SEARCH_USER_RESULT_EMAIL, "")
        }

        usernameView.text = username
        emailView.text = email
    }
}