package com.example.cmpt362project.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
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

class UserProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var nameView: TextView
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

        var nameFromDB: String
        database.child("users").child(user!!.uid).child("name").get()
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
        database.child("users").child(user.uid).child("aboutMe").get()
            .addOnSuccessListener(this) {
                if (it.value == null) {
                    println("UserProfileActivity: aboutMe is ${it.value}!")
                } else {
                    println("UserProfileActivity: Retrieved aboutMe ${it.value} from Firebase")
                    aboutMeFromDB = it.value as String
                    aboutMeView.editText?.setText(aboutMeFromDB)
                    //aboutMeView.text = aboutMeFromDB
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