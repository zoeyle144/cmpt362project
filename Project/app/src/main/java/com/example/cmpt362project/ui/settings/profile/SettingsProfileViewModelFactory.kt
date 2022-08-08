package com.example.cmpt362project.ui.settings.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsProfileViewModelFactory(private val pref: SharedPreferences) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsProfileViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class passed to SettingsProfileViewModelFactory")
    }
}