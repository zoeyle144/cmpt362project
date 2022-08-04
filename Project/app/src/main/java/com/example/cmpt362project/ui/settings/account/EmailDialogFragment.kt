package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.cmpt362project.R

class EmailDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_settings_email_dialog, container, false)


        val toolbar = view.findViewById<Toolbar>(R.id.settings_account_toolbar)
        toolbar.inflateMenu(R.menu.account_settings_toolbar)
        toolbar.setNavigationOnClickListener {
            println("close")
            dismiss()
        }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile_toolbar_save -> {
                    println("Save")
                    true
                }
                else -> {
                    // Do nothing, we shouldn't be here
                    true
                }
            }
        }

        return view
    }
}