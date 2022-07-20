package com.example.cmpt362project.ui.settings

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException.ERROR_OBJECT_NOT_FOUND
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UserProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var nameView: EditText
    private lateinit var aboutMeView: TextInputLayout

    private lateinit var pictureView: ImageView
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var galleryActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        database = Firebase.database.reference
        auth = Firebase.auth
        val user = auth.currentUser

        pictureView = findViewById(R.id.profile_picture)
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        userProfileViewModel.profilePicture.observe(this) { pictureView.setImageBitmap(it) }

        nameView = findViewById(R.id.profile_edit_name)
        aboutMeView = findViewById(R.id.profile_about_me_edit_text_layout)

        database.child("users").child(user!!.uid).child("username").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    val usernameView = findViewById<EditText>(R.id.profile_username)
                    usernameView.setText(it.value as String)
                }
            }

        database.child("users").child(user.uid).child("email").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    val emailView = findViewById<EditText>(R.id.profile_email)
                    emailView.setText(it.value as String)
                }
            }

        database.child("users").child(user.uid).child("name").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    nameView.setText(it.value as String)
                }
            }

        database.child("users").child(user.uid).child("aboutMe").get()
            .addOnSuccessListener(this) {
                if (it.value != null) {
                    aboutMeView.editText?.setText(it.value as String)
                }
            }


        // If cannot find an image, use a place holder


//        val placeholderImage = userProfileViewModel.getImage()
//        if (placeholderImage == null) {
//            println("Placeholder is null, using default")
//            // Don't use drawable, use bitmap
//            // https://github.com/firebase/snippets-android/blob/f29858162c455292d3d18c1cc31d6776b299acbd/storage/app/src/main/java/com/google/firebase/referencecode/storage/kotlin/StorageActivity.kt#L148
//            pictureView.setImageDrawable(getDrawable(R.drawable.ic_launcher_background))
//        } else {
//            println("Placeholder is not null, using view model!")
//            pictureView.setImageBitmap(placeholderImage)
//        }

        val pl2 = downloadAndSetImage()
        if (pl2 == null) {
            println("Could not download pfp, it might be is null. using default")
            // Don't use drawable, use bitmap
            // https://github.com/firebase/snippets-android/blob/f29858162c455292d3d18c1cc31d6776b299acbd/storage/app/src/main/java/com/google/firebase/referencecode/storage/kotlin/StorageActivity.kt#L148
            pictureView.setImageDrawable(getDrawable(R.drawable.ic_launcher_background))
        } else {
            println("pfp is not null, using view model!")
            pictureView.setImageBitmap(pl2)
        }


        // Initialize the gallery activity
        // How to save image inside ViewModel to handle orientation change?
        // https://stackoverflow.com/q/52297555
        galleryActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val resultIntent = it.data
                val resultUri = resultIntent?.data
                val image = BitmapFactory.decodeStream(this.contentResolver.openInputStream(resultUri!!))
                println("Image found. Calling userProfileViewModel.setImage")
                userProfileViewModel.setImage(image)
            }
        }
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
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResult.launch(galleryIntent)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun saveProfile(v: View) {
        val user = auth.currentUser
        val nameToAdd = nameView.text.toString()
        val aboutMeToAdd = aboutMeView.editText?.text.toString()

        database.child("users").child(user!!.uid).child("name").setValue(nameToAdd)
        database.child("users").child(user.uid).child("aboutMe").setValue(aboutMeToAdd)
        uploadImage()

        Toast.makeText(this@UserProfileActivity, "Saved", Toast.LENGTH_SHORT).show()

        finish()
    }

    fun loadProfile(v: View) {

    }

    fun cancelButton(v: View) {
        finish()
    }

    fun uploadImage() {
        val storage = Firebase.storage.reference
        val bitmapToUpload = userProfileViewModel.getImage()
        val user = auth.currentUser

        println("Trying to upload....")
        if (bitmapToUpload != null) {
            println("bitmapToUpload is not null")

            val stream = ByteArrayOutputStream()
            bitmapToUpload.compress(Bitmap.CompressFormat.JPEG, 75, stream)
            val byteArray = stream.toByteArray()
            stream.close()

            val pathString = "profile_pic/" + user!!.uid + ".jpg"
            storage.child(pathString).putBytes(byteArray)
                .addOnSuccessListener(this) { println("Success upload") }
                .addOnFailureListener(this) { println("Failure upload") }
                .addOnCompleteListener(this) { println("Upload complete!") }
        }
    }

    fun downloadAndSetImage() : Bitmap? {
        val storage = Firebase.storage.reference
        val user = auth.currentUser
        val path = "profile_pic/" + user!!.uid + ".jpg"
        val pathReference = storage.child(path)

        val ONE_MEGABYTE: Long = 1024 * 1024
        var bitmap: Bitmap? = null

        try {
            pathReference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(this) {
                    bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    println("Success download!")
                    if (bitmap != null) userProfileViewModel.setImage(bitmap!!)
                } .addOnFailureListener(this) {
                    println("Failure download!")
                }
        } catch (e: Exception) {
            println("UserProfileActivity: Ran into exception after failing to download profile picture")
        }

        return bitmap
    }
}
