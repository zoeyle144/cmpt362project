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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class SettingsProfileActivity : AppCompatActivity() {

    private lateinit var usernameView: TextInputLayout
    private lateinit var emailView: TextInputLayout
    private lateinit var nameView: TextInputLayout
    private lateinit var aboutMeView: TextInputLayout

    private lateinit var pictureView: ImageView
    private lateinit var galleryActivityResult: ActivityResultLauncher<Intent>
    private lateinit var cameraActivityResult: ActivityResultLauncher<Uri>

    private lateinit var viewModelFactory: SettingsProfileViewModelFactory
    private lateinit var viewModel: SettingsProfileViewModel

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

        viewModelFactory = SettingsProfileViewModelFactory(
            getString(R.string.default_pfp_path),
            this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE))
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingsProfileViewModel::class.java]
        pictureView = findViewById(R.id.profile_picture)
        viewModel.profilePicture.observe(this) { pictureView.setImageBitmap(it) }

        usernameView = findViewById(R.id.profile_username_field)
        emailView = findViewById(R.id.profile_email_field)
        nameView = findViewById(R.id.profile_name_field)
        aboutMeView = findViewById(R.id.profile_about_me_field)
        usernameView.isEnabled = false
        emailView.isEnabled = false

        viewModel.setValuesFromDatabase()
        viewModel.usernameViewText.observe(this) {usernameView.editText?.setText(it)}
        viewModel.emailViewText.observe(this) {emailView.editText?.setText(it)}
        viewModel.nameViewText.observe(this) {nameView.editText?.setText(it)}
        viewModel.aboutMeViewText.observe(this) {aboutMeView.editText?.setText(it)}
        if (!viewModel.imageSet) {
            viewModel.profilePicPath.observe(this) {
                ImageUtility.setImageViewToProfilePic(it, pictureView)
            }
        }

        galleryActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val resultIntent = it.data
                val resultUri = resultIntent?.data
                val image = BitmapFactory.decodeStream(this.contentResolver.openInputStream(resultUri!!))
                viewModel.setImage(image)
            }
        }

        cameraActivityResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                if (viewModel.cameraImageUri != Uri.EMPTY) {
                    val image = BitmapFactory.decodeStream(
                        this.contentResolver.openInputStream(viewModel.cameraImageUri))
                    viewModel.setImage(image)
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
        viewModel.cameraImageUri = FileProvider.getUriForFile(this, "com.example.cmpt362project", imageFile)
        cameraActivityResult.launch(viewModel.cameraImageUri)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun saveProfile() {
//        val nameToAdd = nameView.editText?.text.toString()
//        val aboutMeToAdd = aboutMeView.editText?.text.toString()
//
//        viewModel.database.child("users").child(viewModel.user.uid).child("name").setValue(nameToAdd)
//        viewModel.database.child("users").child(viewModel.user.uid).child("aboutMe").setValue(aboutMeToAdd)
//        uploadImage()
//
//        Toast.makeText(this@SettingsProfileActivity, "Saved", Toast.LENGTH_SHORT).show()
//
//        finish()
//    }
//
//    private fun uploadImage() {
//        val printIdentifier = "SettingsProfileActivity uploadImage"
//        val storage = Firebase.storage.reference
//        val image = viewModel.getImage() ?: return
//
//        val imageScaled = Bitmap.createScaledBitmap(image, 240, 240, true)
//        val stream = ByteArrayOutputStream()
//        imageScaled.compress(Bitmap.CompressFormat.JPEG, 75, stream)
//        val byteArray = stream.toByteArray()
//        stream.close()
//
//        // Get the old profile picture path so we can delete the image later (if upload success)
//        val userReference = viewModel.database.child("users").child(viewModel.user.uid).child("profilePic")
//        userReference.get().addOnSuccessListener { oldImgPath ->
//
//            // Write the new profile picture to Storage
//            val randomUUID = UUID.randomUUID().toString().replace("-", "")
//            val newImgPath = "profilePic/$randomUUID.jpg"
//            storage.child(newImgPath).putBytes(byteArray).addOnSuccessListener {
//
//                // Write the new profile picture path to the user's info
//                userReference.setValue(newImgPath).addOnSuccessListener {
//                    println("$printIdentifier: Uploaded $newImgPath to database")
//
//                    // Delete the old profile picture from Storage, tell sidebar to update PFP
//                    // Do not delete the old PFP if it's the default one
//                    val pathToDelete = oldImgPath.value as String
//                    if (pathToDelete != getString(R.string.default_pfp_path)) {
//                        val oldImgRef = storage.child(oldImgPath.value as String)
//                        oldImgRef.delete().addOnSuccessListener {
//                            println("$printIdentifier: Deleted ${oldImgPath.value} from database")
//                            updateProfilePicSharedPref()
//                        }
//                    } else {
//                        updateProfilePicSharedPref()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun updateProfilePicSharedPref() {
//        val sharedPref = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
//        with(sharedPref.edit()) {
//            putBoolean(KEY_PROFILE_PIC_RECENTLY_CHANGED, true)
//            apply()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.profile_toolbar_save -> {
            val nameToAdd = nameView.editText?.text.toString()
            val aboutMeToAdd = aboutMeView.editText?.text.toString()
            viewModel.saveProfile(nameToAdd, aboutMeToAdd)
            finish()
            true
        }
        else -> {
            finish()
            true
        }
    }
}
