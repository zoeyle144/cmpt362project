package com.example.cmpt362project.ui.settings.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsProfileViewModelFactory(private val defaultPFPPath: String,
                                      private val sharedPref: SharedPreferences) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsProfileViewModel(defaultPFPPath, sharedPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class passed to ExerciseViewModelFactory")
    }
}