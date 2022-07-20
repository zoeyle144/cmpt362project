package com.example.cmpt362project.ui.settings

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream

class UserProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var nameView: EditText
    private lateinit var aboutMeView: TextInputLayout

    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        database = Firebase.database.reference
        auth = Firebase.auth
        val user = auth.currentUser

        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        nameView = findViewById(R.id.profile_edit_name)
        aboutMeView = findViewById(R.id.profile_about_me_edit_text_layout)

        database.child("users").child(user!!.uid).child("username").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    val usernameView = findViewById<EditText>(R.id.profile_username)
                    usernameView.setText(it.value as String)
                }
            }

        database.child("users").child(user.uid).child("email").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    val emailView = findViewById<EditText>(R.id.profile_email)
                    emailView.setText(it.value as String)
                }
            }

        database.child("users").child(user.uid).child("name").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    nameView.setText(it.value as String)
                }
            }

        database.child("users").child(user.uid).child("aboutMe").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    aboutMeView.editText?.setText(it.value as String)
                }
            }

    }


    fun onClickChangePhoto(v: View) {
        val builder = AlertDialog.Builder(this)
        val dialogOptions = arrayOf("Open camera", "Select from gallery")

        builder.setTitle("Upload profile picture")
        builder.setItems(dialogOptions) {
                _: DialogInterface, i: Int ->
            if (dialogOptions[i] == "Open camera") {
                println("Camera!")
            }
            else {
                println("Gallery!")
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun saveProfile(v: View) {
        val user = auth.currentUser
        val nameToAdd = nameView.text.toString()
        val aboutMeToAdd = aboutMeView.editText?.text.toString()

        database.child("users").child(user!!.uid).child("name").setValue(nameToAdd)
        database.child("users").child(user.uid).child("aboutMe").setValue(aboutMeToAdd)

        finish()
    }

    fun loadProfile(v: View) {

    }

    fun cancelButton(v: View) {
        finish()
    }
}
