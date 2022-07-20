package com.example.cmpt362project.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference


    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        database = Firebase.database.reference
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        val nameView = findViewById<TextView>(R.id.profile_edit_name)
        val aboutMeView = findViewById<TextView>(R.id.profile_about_me)

        var nameFromDB: String
        database.child("users").child("names").get()
            .addOnSuccessListener(this) {
                if (it.value == null) {
                    println("UserProfileActivity: name is ${it.value}!")
                } else {
                    println("UserProfileActivity: Retrieved name ${it.value} from Firebase")
                    nameFromDB = it.value as String
                    nameView.text = nameFromDB
                }
            }
            .addOnFailureListener(this) {
                println("UserProfileActivity: Failure. Could not find name!")
            }

        var aboutMeFromDB: String
        database.child("users").child("aboutMe").get()
            .addOnSuccessListener(this) {
                if (it.value == null) {
                    println("UserProfileActivity: aboutMe is ${it.value}!")
                } else {
                    println("UserProfileActivity: Retrieved aboutMe ${it.value} from Firebase")
                    aboutMeFromDB = it.value as String
                    aboutMeView.text = aboutMeFromDB
                }
            }
            .addOnFailureListener(this) {
                println("UserProfileActivity: Failure. Could not find aboutMe!")
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
        
    }

    fun loadProfile(v: View) {

    }

    fun cancelButton(v: View) {
        finish()
    }
}