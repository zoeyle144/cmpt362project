package com.example.cmpt362project.ui.settings.profile

import android.graphics.Bitmap
import android.net.Uri
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
    var cameraImageUri = Uri.EMPTY


    private val _usernameViewText = MutableLiveData<String>()
    val usernameViewText: LiveData<String> get() = _usernameViewText

    private val _emailViewText = MutableLiveData<String>()
    val emailViewText: LiveData<String> get() = _emailViewText

    private val _nameViewText = MutableLiveData<String>()
    val nameViewText: LiveData<String> get() = _nameViewText

    private val _aboutMeViewText = MutableLiveData<String>()
    val aboutMeViewText: LiveData<String> get() = _aboutMeViewText

    private val _profilePicPath = MutableLiveData<String>()
    val profilePicPath: LiveData<String> get() = _profilePicPath


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
                _usernameViewText.value = userData["username"] as String
                _emailViewText.value = userData["email"] as String
                _nameViewText.value = userData["name"] as String
                _aboutMeViewText.value = userData["aboutMe"] as String
                _profilePicPath.value = userData["profilePic"] as String
            }
        }
    }
}