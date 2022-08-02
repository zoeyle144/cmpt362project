package com.example.cmpt362project.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object ImageUtility {
    fun setImageViewToProfilePic(pathToProfilePic: String, imageView: ImageView) {
        val storage = Firebase.storage.reference

        val pathReference = storage.child(pathToProfilePic)
        val oneMegabyte: Long = 1024 * 1024

        pathReference.getBytes(oneMegabyte)
            .addOnSuccessListener {
                val bitmap: Bitmap? = BitmapFactory.decodeByteArray(it, 0, it.size)
                if (bitmap != null) imageView.setImageBitmap(bitmap)
            } .addOnFailureListener {
                println("ImageUtility setProfilePicture: Failed to download profile pic")
            }
    }
}