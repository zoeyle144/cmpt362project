package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.cmpt362project.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailDialogFragment : DialogFragment() {

    private var user: FirebaseUser? = null

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

        val view = inflater.inflate(R.layout.account_settings_email_dialog, container, false)

        // Set the "current email" view
        val currentEmailView = view.findViewById<TextInputLayout>(R.id.account_settings_current_email_field)
        currentEmailView.editText!!.setText(user?.email ?: "")
        currentEmailView.isEnabled = false

        val newEmailView = view.findViewById<TextInputLayout>(R.id.account_settings_new_email_field)

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
            if (user != null) {
                // Set a user's email address
                // https://firebase.google.com/docs/auth/android/manage-users#set_a_users_email_address

                // Handle re-authenticate
                // https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user

                // Types of exceptions to check for
                // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser#updateEmail(java.lang.String)
                user!!.updateEmail(newEmail.toString())
                    .addOnSuccessListener {
                        Toast.makeText(context, "Successfully changed email", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error: failed to change email", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
            }
        }

    }

}