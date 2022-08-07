package com.example.cmpt362project.utility

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

object FieldsLayoutUtility {
    fun setListenersForTextInputLayout(views: List<TextInputLayout>) {
        for (i in views) {
            i.editText!!.addTextChangedListener { i.error = null }
            i.editText!!.setOnClickListener { i.error = null }
        }
    }
}