package com.example.cmpt362project.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.cmpt362project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var auth: FirebaseAuth

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_fragment_pref, rootKey)

        auth = Firebase.auth
        val logoutBtn = findPreference<Preference>("logout")

        logoutBtn!!.setOnPreferenceClickListener({ it
            auth.signOut()
            requireActivity().finishAffinity()
            true
        })
    }

}