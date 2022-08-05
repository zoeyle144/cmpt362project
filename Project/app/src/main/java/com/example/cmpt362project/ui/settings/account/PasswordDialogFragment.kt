package com.example.cmpt362project.ui.settings.account

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.cmpt362project.R

class PasswordDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }


}