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
    private var imageSet = false

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

    fun isImageSet() : Boolean {
        return imageSet
    }
}