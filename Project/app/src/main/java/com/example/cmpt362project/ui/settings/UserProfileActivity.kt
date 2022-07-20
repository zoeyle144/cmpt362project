package com.example.cmpt362project.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cmpt362project.R

class UserProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

    }

    fun onClickChangePhoto(v: View) {
        val builder = AlertDialog.Builder(this)
        val dialogOptions = arrayOf("Open camera", "Select from gallery")

        builder.setTitle("Upload profile picture")
        builder.setItems(dialogOptions) {
                _: DialogInterface, i: Int ->
            if (dialogOptions[i] == "Open camera") {
                println("Camera!")
            }
            else {
                println("Gallery!")
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}