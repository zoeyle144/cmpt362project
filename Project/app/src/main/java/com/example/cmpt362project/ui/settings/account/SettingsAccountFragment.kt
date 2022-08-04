package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.cmpt362project.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsAccountFragment : PreferenceFragmentCompat() {

    private val emailDialogFragmentTag = "emailDialogFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_settings_pref, rootKey)

        val auth = Firebase.auth
        val logoutBtn = findPreference<Preference>("logout")
        val emailPreference = findPreference<Preference>("email")

        val user = auth.currentUser
        if (user != null) {
            emailPreference!!.summary = user.email
        }

        emailPreference!!.setOnPreferenceClickListener {
            showEmailDialog()
            true
        }

        logoutBtn!!.setOnPreferenceClickListener {
            auth.signOut()
            requireActivity().finishAffinity()
            true
        }
    }

    private fun showEmailDialog() {
        val newFragment = EmailDialogFragment()
        newFragment.show(childFragmentManager, emailDialogFragmentTag)
    }
}