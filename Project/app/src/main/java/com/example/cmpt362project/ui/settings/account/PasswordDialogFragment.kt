package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.cmpt362project.R
import com.google.android.material.textfield.TextInputLayout

class PasswordDialogFragment : DialogFragment() {

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

        currentPasswordView = view.findViewById(R.id.enter_current_password_field)
        newPasswordView = view.findViewById(R.id.enter_new_password_field)
        reNewPasswordView = view.findViewById(R.id.reenter_new_password_field)

        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_toolbar)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile_toolbar_save -> {
                    val newPass = newPasswordView.editText!!.text.toString()
                    val reNewPass = newPasswordView.editText!!.text.toString()
                    setNewPassword(newPass, reNewPass)
                    true
                }
                else -> true
            }
        }

        return view
    }

    private fun setNewPassword(newPassword: String, reNewPassword: String) {

    }
}