package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.cmpt362project.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsAccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_settings_pref, rootKey)

        val auth = Firebase.auth
        val logoutBtn = findPreference<Preference>("logout")
        val emailPreference = findPreference<Preference>("email")

        val user = auth.currentUser
        if (user != null) {
            emailPreference!!.summary = user.email
        }

        logoutBtn!!.setOnPreferenceClickListener {
            auth.signOut()
            requireActivity().finishAffinity()
            true
        }
    }
}