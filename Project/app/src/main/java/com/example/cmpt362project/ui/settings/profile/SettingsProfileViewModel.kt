package com.example.cmpt362project.ui.settings.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.ui.settings.SingleLiveEvent

class SettingsProfileViewModel : ViewModel() {

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