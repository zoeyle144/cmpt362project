package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.cmpt362project.R

class SettingsAccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}