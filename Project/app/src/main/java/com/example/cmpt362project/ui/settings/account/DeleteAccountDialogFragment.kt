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

class DeleteAccountDialogFragment : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var viewModel: ReAuthenticator

    private lateinit var currentEmailView: TextInputLayout
    private lateinit var currentPasswordView: TextInputLayout
    private lateinit var confirmView: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_settings_delete_dialog, container, false)

        auth = Firebase.auth
        user = auth.currentUser
        viewModel = ViewModelProvider(requireActivity())[ReAuthenticator::class.java]

        currentEmailView = view.findViewById(R.id.enter_current_email_field)
        currentPasswordView = view.findViewById(R.id.enter_current_password_field)
        confirmView = view.findViewById(R.id.enter_confirm_field)

        currentEmailView.editText!!.addTextChangedListener { currentEmailView.error = null }
        currentPasswordView.editText!!.addTextChangedListener { currentPasswordView.error = null }
        confirmView.editText!!.addTextChangedListener { confirmView.error = null }

        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_delete_acc_toolbar)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.account_settings_toolbar_confirm -> {
                    val currEmail = currentEmailView.editText!!.text.toString()
                    val currPass = currentPasswordView.editText!!.text.toString()
                    val confirmText = confirmView.editText!!.text.toString()
                    deleteAccount(currEmail, currPass, confirmText)
                    true
                }
                else -> true
            }
        }

        return view
    }

    private fun deleteAccount(currEmail: String, currPass: String, confirmText: String) {
        var checkFields = true
        if (currEmail.isEmpty()) {
            currentEmailView.error = "E-mail field cannot be empty."
            checkFields = false
        }
        if (currPass.isEmpty()) {
            currentPasswordView.error = "Password field cannot be empty."
            checkFields = false
        }
        if (confirmText != "CONFIRM") {
            confirmView.error = "Text entered is not equal to CONFIRM."
            checkFields = false
        }
        if (!checkFields) return

        viewModel.reAuthenticateCheckEmail(user, currEmail, currPass).observe(viewLifecycleOwner) { waitBoolean ->
            when(waitBoolean) {
                ReAuthenticateBoolean.FAILURE -> {
                    currentEmailView.error = "E-mail may be incorrect."
                    currentPasswordView.error = "Password may be incorrect."
                }
                ReAuthenticateBoolean.SUCCESS -> {
                    if (user != null) {
                        user!!.delete().addOnSuccessListener {
                            Toast.makeText(context, "Successfully deleted account.", Toast.LENGTH_SHORT).show()
                            requireActivity().finishAffinity()
                        } .addOnFailureListener {
                            Toast.makeText(context, "Couldn't delete account. ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else -> {}
            }
        }
    }
}