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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DeleteAccountDialogFragment : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var viewModel: ReAuthenticator

    private lateinit var currentEmailView: TextInputLayout
    private lateinit var currentPasswordView: TextInputLayout
    private lateinit var confirmView: TextInputLayout

    private lateinit var database: DatabaseReference

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

        database = Firebase.database.reference
        auth = Firebase.auth
        user = auth.currentUser
        viewModel = ViewModelProvider(requireActivity())[ReAuthenticator::class.java]

        currentEmailView = view.findViewById(R.id.enter_current_email_field)
        currentPasswordView = view.findViewById(R.id.enter_current_password_field)
        confirmView = view.findViewById(R.id.enter_confirm_field)

        currentEmailView.editText!!.addTextChangedListener { currentEmailView.error = null }
        currentPasswordView.editText!!.addTextChangedListener { currentPasswordView.error = null }
        confirmView.editText!!.addTextChangedListener { confirmView.error = null }
        currentEmailView.editText!!.setOnClickListener { currentEmailView.error = null }
        currentPasswordView.editText!!.setOnClickListener { currentPasswordView.error = null }
        confirmView.editText!!.setOnClickListener { confirmView.error = null }

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
                    if (user != null) deleteProfilePicFromStorage(user!!)
                    else Toast.makeText(context, "Couldn't delete account. User is null", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    private fun deleteProfilePicFromStorage(user: FirebaseUser) {
        val storage = Firebase.storage.reference

        // Get the info in the user's profilePic field in Realtime Database
        val userReference = database.child("users").child(user.uid).child("profilePic")
        userReference.get().addOnSuccessListener { imagePathDB ->

            // Get a reference to the profile picture in Storage
            val imagePathStorage = imagePathDB.value as String

            // If the profile picture is custom, delete it, then delete fields
            if (imagePathStorage != getString(R.string.default_pfp_path)) {
                val imageRef = storage.child(imagePathStorage)
                imageRef.delete().addOnSuccessListener {
                    println("DeleteAccountDialogFragment: Deleted file $imagePathStorage from Storage")
                    deleteFieldsFromRealtimeDatabase(user)
                }
            }
            // If the profile picture is default, skip deleting it. Just delete fields
            else deleteFieldsFromRealtimeDatabase(user)
        }
    }

    private fun deleteFieldsFromRealtimeDatabase(user: FirebaseUser) {
        database.child("users").child(user.uid).removeValue().addOnSuccessListener {
            println("DeleteAccountDialogFragment: Deleted fields of user ${user.uid} from Realtime Database")
            deleteUserFromAuthentication(user)
        }
    }

    private fun deleteUserFromAuthentication(user: FirebaseUser) {
        val userID = user.uid
        user.delete().addOnSuccessListener {
            println("DeleteAccountDialogFragment: Deleted user $userID from Authentication")
            Toast.makeText(context, "Successfully deleted account.", Toast.LENGTH_SHORT).show()
            requireActivity().finishAffinity()
        } .addOnFailureListener {
            Toast.makeText(context, "Couldn't delete user from Authentication. ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}