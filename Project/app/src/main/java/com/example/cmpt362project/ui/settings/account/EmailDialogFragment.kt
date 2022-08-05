package com.example.cmpt362project.ui.settings.account

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
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
    private lateinit var newEmailView: TextInputLayout
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

        newEmailView = view.findViewById(R.id.account_settings_new_email_field)
        passwordView = view.findViewById(R.id.account_settings_password_field)

        newEmailView.editText!!.addTextChangedListener { newEmailView.error = null }
        passwordView.editText!!.addTextChangedListener { passwordView.error = null }

        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_toolbar)

        // Back button
        toolbar.setNavigationOnClickListener { dismiss() }

        // Save button
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile_toolbar_save -> {
                    setNewEmail(newEmailView.editText!!.text.toString())
                    true
                }
                else -> true
            }
        }

        return view
    }


    private fun setNewEmail(newEmail: String) {
        val password = passwordView.editText!!.text.toString()

        viewModel.reAuthenticate(user, password).observe(viewLifecycleOwner) { waitBoolean ->
            when (waitBoolean) {
                EmailDialogFragmentViewModel.WaitBoolean.FALSE -> {
                    passwordView.error = "Password is incorrect."
                }
                EmailDialogFragmentViewModel.WaitBoolean.TRUE -> {
                    if (user != null) {
                        if (newEmail.isNotEmpty()) {
                            user!!.updateEmail(newEmail)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Successfully changed e-mail.", Toast.LENGTH_SHORT).show()
                                    updateNewEmailSharedPref()
                                    dismiss()
                                }
                                .addOnFailureListener {
                                    newEmailView.error = it.message
                                }
                        } else {
                            newEmailView.error = "E-mail field cannot be empty."
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun updateNewEmailSharedPref() {
        if (activity != null) {
            val sharedPref = activity!!.getSharedPreferences(SettingsProfileActivity.SHARED_PREF, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(KEY_EMAIL_RECENTLY_CHANGED, true)
                apply()
            }
        }
    }

}