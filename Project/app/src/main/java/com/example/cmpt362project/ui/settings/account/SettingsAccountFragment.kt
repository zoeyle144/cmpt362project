package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.cmpt362project.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsAccountFragment : PreferenceFragmentCompat() {

    private val emailDialogFragmentTag = "emailDialogFragmentTag"
    private val passwordDialogFragmentTag = "passwordDialogFragmentTag"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_settings_pref, rootKey)

        val auth = Firebase.auth

        val logoutPreference = findPreference<Preference>("logout")
        val emailPreference = findPreference<Preference>("email")
        val passwordPreference = findPreference<Preference>("password")

        logoutPreference!!.setOnPreferenceClickListener {
            auth.signOut()
            requireActivity().finishAffinity()
            true
        }

        emailPreference!!.setOnPreferenceClickListener {
            showEmailDialog()
            true
        }

        passwordPreference!!.setOnPreferenceClickListener {
            showPasswordDialog()
            true
        }
    }

    private fun showEmailDialog() {
        val emailDialogFragment = EmailDialogFragment()
        emailDialogFragment.show(childFragmentManager, emailDialogFragmentTag)
    }

    private fun showPasswordDialog() {
        val passwordDialogFragment = PasswordDialogFragment()
        passwordDialogFragment.show(childFragmentManager, passwordDialogFragmentTag)
    }
}