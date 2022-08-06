package com.example.cmpt362project.ui.settings.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.ui.settings.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingsProfileViewModel : ViewModel() {

    val database: DatabaseReference = Firebase.database.reference
    val auth: FirebaseAuth = Firebase.auth
    val user: FirebaseUser = auth.currentUser!!

    val profilePicture = MutableLiveData<Bitmap>()
    var imageSet = false
        private set

    var usernameViewText = ""
        private set
    var emailViewText = ""
        private set
    var nameViewText = ""
        private set
    var aboutMeViewText = ""
        private set
    var profilePicPath = ""
        private set

    private val _toastMessage = MutableLiveData<SingleLiveEvent<String>>()
    val toastMessage : LiveData<SingleLiveEvent<String>>
        get() = _toastMessage

    fun getImage() : Bitmap? {
        return if(imageSet) profilePicture.value
        else null
    }

    fun setImage(bitmap: Bitmap) {
        profilePicture.value = bitmap
        imageSet = true
    }

    fun setValuesFromDatabase() {
        database.child("users").child(user.uid).get().addOnSuccessListener {
            if (it != null) {
                val userData = it.value as Map<*, *>
                usernameViewText = userData["username"] as String
                emailViewText = userData["email"] as String
                nameViewText = userData["name"] as String
                aboutMeViewText = userData["aboutMe"] as String
                profilePicPath = userData["profilePic"] as String
            }
        }
    }
}