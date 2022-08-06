package com.example.cmpt362project.ui.settings.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.R
import com.example.cmpt362project.ui.settings.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*

class SettingsProfileViewModel : ViewModel() {

    val database: DatabaseReference = Firebase.database.reference
    val auth: FirebaseAuth = Firebase.auth
    val user: FirebaseUser = auth.currentUser!!

    val profilePicture = MutableLiveData<Bitmap>()
    var imageSet = false
        private set
    var cameraImageUri = Uri.EMPTY!!

    private val _usernameViewText = MutableLiveData<String>()
    val usernameViewText: LiveData<String> get() = _usernameViewText

    private val _emailViewText = MutableLiveData<String>()
    val emailViewText: LiveData<String> get() = _emailViewText

    private val _nameViewText = MutableLiveData<String>()
    val nameViewText: LiveData<String> get() = _nameViewText

    private val _aboutMeViewText = MutableLiveData<String>()
    val aboutMeViewText: LiveData<String> get() = _aboutMeViewText

    private val _profilePicPath = MutableLiveData<String>()
    val profilePicPath: LiveData<String> get() = _profilePicPath


    private val _toastMessage = MutableLiveData<SingleLiveEvent<String>>()
    val toastMessage : LiveData<SingleLiveEvent<String>>
        get() = _toastMessage

    fun getImage() : Bitmap? {
        return if(imageSet) profilePicture.value
        else null
    }

    fun setImage(bitmap: Bitmap) {
        profilePicture.value = bitmap
        imageSet = true
    }

    fun setValuesFromDatabase() {
        database.child("users").child(user.uid).get().addOnSuccessListener {
            if (it != null) {
                val userData = it.value as Map<*, *>
                _usernameViewText.value = userData["username"] as String
                _emailViewText.value = userData["email"] as String
                _nameViewText.value = userData["name"] as String
                _aboutMeViewText.value = userData["aboutMe"] as String
                _profilePicPath.value = userData["profilePic"] as String
            }
        }
    }

    fun saveProfile(name: String, aboutMe: String) {
        database.child("users").child(user.uid).child("name").setValue(name).addOnSuccessListener {
            database.child("users").child(user.uid).child("aboutMe").setValue(aboutMe).addOnSuccessListener {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        val printIdentifier = "SettingsProfileActivity uploadImage"
        val storage = Firebase.storage.reference
        val image = getImage() ?: return

        val imageScaled = Bitmap.createScaledBitmap(image, 240, 240, true)
        val stream = ByteArrayOutputStream()
        imageScaled.compress(Bitmap.CompressFormat.JPEG, 75, stream)
        val byteArray = stream.toByteArray()
        stream.close()

        // Get the old profile picture path so we can delete the image later (if upload success)
        val userReference = database.child("users").child(user.uid).child("profilePic")
        userReference.get().addOnSuccessListener { oldImgPath ->

            // Write the new profile picture to Storage
            val randomUUID = UUID.randomUUID().toString().replace("-", "")
            val newImgPath = "profilePic/$randomUUID.jpg"
            storage.child(newImgPath).putBytes(byteArray).addOnSuccessListener {

                // Write the new profile picture path to the user's info
                userReference.setValue(newImgPath).addOnSuccessListener {
                    println("$printIdentifier: Uploaded $newImgPath to database")

                    // Delete the old profile picture from Storage, tell sidebar to update PFP
                    // Do not delete the old PFP if it's the default one
                    val pathToDelete = oldImgPath.value as String
                    if (pathToDelete != getString(R.string.default_pfp_path)) {
                        val oldImgRef = storage.child(oldImgPath.value as String)
                        oldImgRef.delete().addOnSuccessListener {
                            println("$printIdentifier: Deleted ${oldImgPath.value} from database")
                            updateProfilePicSharedPref()
                        }
                    } else {
                        updateProfilePicSharedPref()
                    }
                }
            }
        }
    }

    private fun updateProfilePicSharedPref() {
        val sharedPref = this.getSharedPreferences(SettingsProfileActivity.SHARED_PREF, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(com.example.cmpt362project.ui.settings.profile.SettingsProfileActivity.KEY_PROFILE_PIC_RECENTLY_CHANGED, true)
            apply()
        }
    }
}