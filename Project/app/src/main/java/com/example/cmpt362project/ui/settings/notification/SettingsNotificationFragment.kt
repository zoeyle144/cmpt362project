package com.example.cmpt362project.ui.settings.notification

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.cmpt362project.R

class SettingsNotificationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_settings_pref, rootKey)
    }
}