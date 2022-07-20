package com.example.cmpt362project.ui.settings

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {
    val profilePicture = MutableLiveData<Bitmap>()

    private lateinit var tempImage: Bitmap
    var imageSet = false

    fun getImage() : Bitmap? {
        return if(imageSet) {
            tempImage
        } else {
            null
        }
    }

    fun setImage(bitmap: Bitmap) {
        tempImage = bitmap
        imageSet = true
    }
}