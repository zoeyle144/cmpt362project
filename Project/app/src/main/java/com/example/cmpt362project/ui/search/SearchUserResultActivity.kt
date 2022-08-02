package com.example.cmpt362project.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.cmpt362project.R
import com.example.cmpt362project.utility.ImageUtility
import com.google.android.material.textfield.TextInputLayout

class SearchUserResultActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH_USER_RESULT_USERNAME = "KEY_SEARCH_USER_RESULT_USERNAME"
        const val KEY_SEARCH_USER_RESULT_EMAIL = "KEY_SEARCH_USER_RESULT_EMAIL"
        const val KEY_SEARCH_USER_RESULT_NAME = "KEY_SEARCH_USER_RESULT_NAME"
        const val KEY_SEARCH_USER_RESULT_PROFILE_PIC = "KEY_SEARCH_USER_RESULT_PROFILE_PIC"
        const val KEY_SEARCH_USER_RESULT_ABOUT_ME = "KEY_SEARCH_USER_RESULT_ABOUT_ME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val toolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var username = ""
        var email = ""
        var name = ""
        var profilePic = ""
        var aboutMe = ""

        val usernameView = findViewById<TextInputLayout>(R.id.profile_username_field)
        val emailView = findViewById<TextInputLayout>(R.id.profile_email_field)
        val nameView = findViewById<TextInputLayout>(R.id.profile_name_field)
        val pictureView = findViewById<ImageView>(R.id.profile_picture)
        val aboutMeView = findViewById<TextInputLayout>(R.id.profile_about_me_field)

        val extras = intent.extras
        if (extras != null) {
            username = extras.getString(KEY_SEARCH_USER_RESULT_USERNAME, "")
            email = extras.getString(KEY_SEARCH_USER_RESULT_EMAIL, "")
            name = extras.getString(KEY_SEARCH_USER_RESULT_NAME, "")
            profilePic = extras.getString(KEY_SEARCH_USER_RESULT_PROFILE_PIC, "")
            aboutMe = extras.getString(KEY_SEARCH_USER_RESULT_ABOUT_ME, "")
        }

        usernameView.editText?.setText(username)
        emailView.editText?.setText(email)
        nameView.editText?.setText(name)
        ImageUtility.setImageViewToProfilePic(profilePic, pictureView)
        aboutMeView.editText?.setText(aboutMe)
    }
}