package com.example.cmpt362project.ui.settings.account

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.ui.settings.profile.SettingsProfileActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred

class EmailDialogFragment : DialogFragment() {

    private var user: FirebaseUser? = null
    private lateinit var passwordView: TextInputLayout
    private lateinit var viewModel: EmailDialogFragmentViewModel

    companion object {
        const val KEY_EMAIL_RECENTLY_CHANGED = "KEY_EMAIL_RECENTLY_CHANGED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = Firebase.auth.currentUser
        viewModel = ViewModelProvider(requireActivity())[EmailDialogFragmentViewModel::class.java]

        val view = inflater.inflate(R.layout.account_settings_email_dialog, container, false)

        // Set the "current email" view
        val currentEmailView = view.findViewById<TextInputLayout>(R.id.account_settings_current_email_field)
        currentEmailView.editText!!.setText(user?.email ?: "")
        currentEmailView.isEnabled = false

        val newEmailView = view.findViewById<TextInputLayout>(R.id.account_settings_new_email_field)
        passwordView = view.findViewById(R.id.account_settings_password_field)

        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_toolbar)

        // Back button
        toolbar.setNavigationOnClickListener { dismiss() }

        // Save button
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile_toolbar_save -> {
                    setNewEmail(newEmailView.editText!!.text)
                    true
                }
                else -> true
            }
        }

        return view
    }


    private fun setNewEmail(newEmail: Editable) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            Toast.makeText(context, "Error: invalid email", Toast.LENGTH_SHORT)
        } else if (newEmail.isEmpty()) {
            Toast.makeText(context, "Error: empty email", Toast.LENGTH_SHORT)
        } else {
            val pass = passwordView.editText!!.text.toString()
            viewModel.reAuthenticate(user, pass).observe(viewLifecycleOwner) { waitBoolean ->
                when (waitBoolean) {
                    EmailDialogFragmentViewModel.WaitBoolean.FALSE -> {
                        Toast.makeText(context, "Error: re-authentication failed. Check your password", Toast.LENGTH_SHORT).show()
                    }
                    EmailDialogFragmentViewModel.WaitBoolean.TRUE -> {
                        if (user != null) {
                            user!!.updateEmail(newEmail.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Successfully changed email", Toast.LENGTH_SHORT).show()
                                    updateNewEmailSharedPref()
                                    dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error: failed to change email", Toast.LENGTH_SHORT).show()
                                    println(it.message)
                                    dismiss()
                                }
                        }
                    }
                    else -> {}
                }
            }

            if (false) {
                if (false) {
                    // Set a user's email address
                    // https://firebase.google.com/docs/auth/android/manage-users#set_a_users_email_address

                    // Handle re-authenticate
                    // https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user

                    // Types of exceptions to check for
                    // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser#updateEmail(java.lang.String)

                } else {
                    Toast.makeText(context, "Error: re-authentication failed. Check your password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /*
         TODO: - doesn't check for password
         TODO: - SettingsAccountFragment doesn't update its field based on the new email
         TODO: - user profile (and Realtime Database) don't get the new e-mail
         */
    }

    private fun updateNewEmailSharedPref() {
        if (activity != null) {
            val sharedPref = activity!!.getSharedPreferences(SettingsProfileActivity.SHARED_PREF, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(KEY_EMAIL_RECENTLY_CHANGED, true)
                apply()
            }
            println("updateNewEmailSharedPref called")
        }
    }

}