package com.example.cmpt362project.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.BoardUpdateData
import com.example.cmpt362project.utility.ImageUtility
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.google.android.material.button.MaterialButton
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


class DisplayBoardInfoActivity: AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var pictureView: ImageView
    private lateinit var galleryActivityResult: ActivityResultLauncher<Intent>
    private lateinit var cameraActivityResult: ActivityResultLauncher<Uri>
    private lateinit var cameraImageUri: Uri
    private lateinit var boardListViewModel: BoardListViewModel

    companion object {
        const val BOARD_PIC_RECENTLY_CHANGED = "BOARD_PIC_RECENTLY_CHANGED"
        const val SHARED_PREF = "SHARED_PREF"

        const val REQUEST_CAMERA_PERMISSION_CODE = 100
        const val CAMERA_SAVED_FILE_NAME = "temp_bp.jpg"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_board_info)

        val sp = getSharedPreferences("SharedPrefForBoardImage", MODE_PRIVATE)
        var imageSet = sp.getBoolean("imageSet", false)

        val boardParcel = intent.getParcelableExtra<Board>("board")
        val boardID = boardParcel?.boardID.toString()
        val boardName = boardParcel?.boardName.toString()
        val boardDescription = boardParcel?.boardName.toString()
        var boardPicString = boardParcel?.boardPic.toString()
        if (boardPicString == ""){
            boardPicString = getString(R.string.default_bp_path)
        }

        pictureView = findViewById(R.id.board_picture)
        val boardNameField = findViewById<EditText>(R.id.board_info_name_input)
        val boardDescriptionField = findViewById<EditText>(R.id.board_info_description_input)
        val saveButton = findViewById<Button>(R.id.save_board_info_button)
        val closeButton = findViewById<Button>(R.id.close_board_info_button)
        val changeBoardPicButton = findViewById<MaterialButton>(R.id.board_picture_change_picture_button)

        if (MainActivity.role != "admin"){
            boardNameField.isEnabled = false
            boardDescriptionField.isEnabled = false
            saveButton.isEnabled = false
            changeBoardPicButton.isEnabled = false
        }

        boardNameField.setText(boardName)
        boardDescriptionField.setText(boardDescription)



        saveButton.setOnClickListener{
            val boardUpdateData = BoardUpdateData(boardNameField.text.toString(), boardDescriptionField.text.toString())
            boardListViewModel = ViewModelProvider(this)[boardListViewModel::class.java]
            boardListViewModel.update(boardID, boardUpdateData)
//            val intentData = Intent()
//            intentData.putExtra("boardNameField", boardNameField.text.toString())
//            setResult(RESULT_OK, intentData)
            setResult(RESULT_OK, null)
            uploadImage(boardID)
            finish()
        }

        closeButton.setOnClickListener{
            finish()
        }

        database = Firebase.database.reference
        auth = Firebase.auth
        user = auth.currentUser!!

        boardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
        println("debug: imageSet? $imageSet")

        boardListViewModel.boardPic.observe(this) { pictureView.setImageBitmap(it) }

        if (!imageSet){
            ImageUtility.setImageViewToProfilePic(boardPicString, pictureView)
        }

        galleryActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val resultIntent = it.data
                val resultUri = resultIntent?.data
                val image = BitmapFactory.decodeStream(this.contentResolver.openInputStream(resultUri!!))
                println("Image found. Calling boardListViewModel.setImage")
                boardListViewModel.setImage(image)
                imageSet = true
                val spEditor = sp.edit()
                spEditor.putBoolean("imageSet", true)
                spEditor.apply()
            }
        }

        cameraImageUri = Uri.EMPTY
        cameraActivityResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                if (cameraImageUri != Uri.EMPTY) {
                    val image = BitmapFactory.decodeStream(this.contentResolver.openInputStream(cameraImageUri))
                    boardListViewModel.setImage(image)
                    imageSet = true
                    val spEditor = sp.edit()
                    spEditor.putBoolean("imageSet", true)
                    spEditor.apply()
                }
            } else {
                Toast.makeText(this, "Failed to get image from camera", Toast.LENGTH_SHORT).show()
            }
        }

        changeBoardPicButton.setOnClickListener{
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            val dialogOptions = arrayOf("Open camera", "Select from gallery")

            builder.setTitle("Upload board picture")
            builder.setItems(dialogOptions) {
                    _: DialogInterface, i: Int ->
                if (dialogOptions[i] == "Open camera") {
                    println("Camera!")
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION_CODE
                        )
                    } else {launchCamera()}
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

    }

    private fun launchCamera() {
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
            } else println("Camera permission denied!")
        }
    }

    private fun uploadImage(boardID:String) {
        val printIdentifier = "DisplayBoardInfoActivity uploadImage"
        val storage = Firebase.storage.reference
        val image = boardListViewModel.getImage() ?: return

        val imageScaled = Bitmap.createScaledBitmap(image, 240, 240, true)
        val stream = ByteArrayOutputStream()
        imageScaled.compress(Bitmap.CompressFormat.JPEG, 75, stream)
        val byteArray = stream.toByteArray()
        stream.close()

        // Get the old profile picture path so we can delete the image later (if upload success)
        val boardReference = database.child("boards").child(boardID).child("boardPic")
        boardReference.get().addOnSuccessListener { oldImgPath ->

            // Write the new profile picture to Storage
            val randomUUID = UUID.randomUUID().toString().replace("-", "")
            val newImgPath = "boardPic/$randomUUID.jpg"
            storage.child(newImgPath).putBytes(byteArray).addOnSuccessListener {

                // Write the new profile picture path to the user's info
                boardReference.setValue(newImgPath).addOnSuccessListener {
                    println("$printIdentifier: Uploaded $newImgPath to database")

                    // Delete the old profile picture from Storage, tell sidebar to update PFP
                    // Do not delete the old PFP if it's the default one
                    val pathToDelete = oldImgPath.value as String
                    if (pathToDelete != getString(R.string.default_bp_path) && pathToDelete!="") {
                        val oldImgRef = storage.child(pathToDelete)
                        oldImgRef.delete().addOnSuccessListener {
                            println("$printIdentifier: Deleted ${oldImgPath.value} from database")
                            updateProfilePicSharedPref()
                        }
                    }else{
                        updateProfilePicSharedPref()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val boardTitle  = intent.getParcelableExtra<Board>("board")?.boardName.toString()
        menuInflater.inflate(R.menu.custom_board_info_menu,menu)
        getSupportActionBar()?.setTitle("$boardTitle");
        return super.onCreateOptionsMenu(menu)
    }

    private fun updateProfilePicSharedPref() {
        val sharedPref = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(BOARD_PIC_RECENTLY_CHANGED, true)
            apply()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.delete_board_button){
            if (MainActivity.role == "admin"){
                val confirmationBuilder = AlertDialog.Builder(this)
                val selectedBoard = intent.getParcelableExtra<Board>("board")
                confirmationBuilder.setMessage("Are you sure you want to Delete Board <${selectedBoard?.boardName}>?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        val boardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
                        boardListViewModel.delete(selectedBoard?.boardID.toString(), selectedBoard?.boardName.toString(), "-N8nxMBOuplwJJx6iNFn")
                        setResult(RESULT_OK, null)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = confirmationBuilder.create()
                alert.show()
            }else{
                Toast.makeText(this,
                    "You do not have permission to delete board",
                    Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}