package com.example.cmpt362project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val emailTextForDrawer = MutableLiveData<String>()
}