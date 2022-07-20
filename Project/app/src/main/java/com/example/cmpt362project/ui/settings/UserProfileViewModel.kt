package com.example.cmpt362project.ui.settings

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {
    val profilePicture = MutableLiveData<Bitmap>()
    val profileName = MutableLiveData<String>()
    val profileAboutMe = MutableLiveData<String>()
}