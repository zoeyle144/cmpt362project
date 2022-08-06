package com.example.cmpt362project.ui.settings.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.utility.ImageUtility
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class SettingsProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var usernameView: TextInputLayout
    private lateinit var emailView: TextInputLayout
    private lateinit var nameView: TextInputLayout
    private lateinit var aboutMeView: TextInputLayout

    private lateinit var pictureView: ImageView
    private lateinit var userProfileViewModel: SettingsProfileViewModel
    private lateinit var galleryActivityResult: ActivityResultLauncher<Intent>
    private lateinit var cameraActivityResult: ActivityResultLauncher<Uri>
    private lateinit var cameraImageUri: Uri

    companion object {
        const val KEY_PROFILE_PIC_RECENTLY_CHANGED = "KEY_PROFILE_PIC_RECENTLY_CHANGED"
        const val SHARED_PREF = "SHARED_PREF"

        const val REQUEST_CAMERA_PERMISSION_CODE = 100
        const val CAMERA_SAVED_FILE_NAME = "temp_pfp.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_profile)

        val toolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.profile_toolbar_title_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_baseline_close_24, theme))
        supportActionBar?.setHomeActionContentDescription(getString(R.string.profile_toolbar_discard))

        database = Firebase.database.reference
        auth = Firebase.auth
        user = auth.currentUser!!

        pictureView = findViewById(R.id.profile_picture)
        userProfileViewModel = ViewModelProvider(this).get(SettingsProfileViewModel::class.java)
        userProfileViewModel.profilePicture.observe(this) { pictureView.setImageBitmap(it) }

        usernameView = findViewById(R.id.profile_username_field)
        emailView = findViewById(R.id.profile_email_field)
        nameView = findViewById(R.id.profile_name_field)
        aboutMeView = findViewById(R.id.profile_about_me_field)
        usernameView.isEnabled = false
        emailView.isEnabled = false

        database.child("users").child(user.uid).get()
            .addOnSuccessListener {
                if (it != null) {
                    val userData = it.value as Map<*, *>

                    usernameView.editText?.setText(userData["username"] as String)
                    emailView.editText?.setText(userData["email"] as String)
                    nameView.editText?.setText(userData["name"] as String)
                    aboutMeView.editText?.setText(userData["aboutMe"] as String)
                    ImageUtility.setImageViewToProfilePic(userData["profilePic"] as String, pictureView)
                }
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

        cameraActivityResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                if (cameraImageUri != Uri.EMPTY) {
                    val image = BitmapFactory.decodeStream(this.contentResolver.openInputStream(cameraImageUri))
                    userProfileViewModel.setImage(image)
                }
            } else {
                Toast.makeText(this, "Failed to get image from camera", Toast.LENGTH_SHORT).show()
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
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION_CODE)
                } else launchCamera()
            }
            else {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResult.launch(galleryIntent)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun launchCamera() {
        // Store image in app's storage
        val imageFile = File(getExternalFilesDir(null), CAMERA_SAVED_FILE_NAME)
        cameraImageUri = FileProvider.getUriForFile(this, "com.example.cmpt362project", imageFile)
        cameraActivityResult.launch(cameraImageUri)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Camera permission granted")
                launchCamera()
            } else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveProfile() {
        val user = auth.currentUser
        val nameToAdd = nameView.editText?.text.toString()
        val aboutMeToAdd = aboutMeView.editText?.text.toString()

        database.child("users").child(user!!.uid).child("name").setValue(nameToAdd)
        database.child("users").child(user.uid).child("aboutMe").setValue(aboutMeToAdd)
        uploadImage()

        Toast.makeText(this@SettingsProfileActivity, "Saved", Toast.LENGTH_SHORT).show()

        finish()
    }

    private fun uploadImage() {
        val printIdentifier = "SettingsProfileActivity uploadImage"
        val storage = Firebase.storage.reference
        val image = userProfileViewModel.getImage() ?: return

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
        val sharedPref = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(KEY_PROFILE_PIC_RECENTLY_CHANGED, true)
            apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.profile_toolbar_save -> {
            saveProfile()
            true
        }
        else -> {
            finish()
            true
        }
    }
}
