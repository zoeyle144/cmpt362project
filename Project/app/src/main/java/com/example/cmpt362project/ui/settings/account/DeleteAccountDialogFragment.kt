package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.cmpt362project.R

class DeleteAccountDialogFragment : DialogFragment() {

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


        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_delete_acc_toolbar)
        toolbar.setNavigationOnClickListener { dismiss() }

        return view
    }
}