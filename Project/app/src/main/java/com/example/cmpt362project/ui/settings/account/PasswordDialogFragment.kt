package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordDialogFragment : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var viewModel: ReAuthenticator

    private lateinit var currentPasswordView: TextInputLayout
    private lateinit var newPasswordView: TextInputLayout
    private lateinit var reNewPasswordView: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_settings_password_dialog, container, false)

        auth = Firebase.auth
        user = auth.currentUser
        viewModel = ViewModelProvider(requireActivity())[ReAuthenticator::class.java]

        currentPasswordView = view.findViewById(R.id.enter_current_password_field)
        newPasswordView = view.findViewById(R.id.enter_new_password_field)
        reNewPasswordView = view.findViewById(R.id.reenter_new_password_field)

        currentPasswordView.editText!!.addTextChangedListener { currentPasswordView.error = null }
        newPasswordView.editText!!.addTextChangedListener { newPasswordView.error = null }
        reNewPasswordView.editText!!.addTextChangedListener { reNewPasswordView.error = null }

        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_toolbar)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile_toolbar_save -> {
                    val currentPass = currentPasswordView.editText!!.text.toString()
                    val newPass = newPasswordView.editText!!.text.toString()
                    val reNewPass = reNewPasswordView.editText!!.text.toString()
                    setNewPassword(currentPass, newPass, reNewPass)
                    true
                }
                else -> true
            }
        }

        return view
    }

    private fun setNewPassword(currPass: String, newPass: String, reNewPass: String) {
        if (currPass.isEmpty()) {
            currentPasswordView.error = "Current password field cannot be empty"
            return
        }

        viewModel.reAuthenticate(user, currPass).observe(viewLifecycleOwner) { waitBoolean ->
            when(waitBoolean) {
                ReAuthenticateBoolean.FAILURE -> currentPasswordView.error = "Current password is incorrect."
                ReAuthenticateBoolean.SUCCESS -> {
                    if (user != null) {
                        if (newPass.isEmpty()) newPasswordView.error = "Enter new password field cannot empty."
                        else if (newPass != reNewPass) reNewPasswordView.error = "Does not match the new password field."
                        else {
                            user!!.updatePassword(newPass).addOnSuccessListener {
                                Toast.makeText(context, "Successfully changed password.", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                requireActivity().finishAffinity()
                            }.addOnFailureListener {
                                newPasswordView.error = (it as FirebaseAuthWeakPasswordException).reason + "."
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}