package com.example.cmpt362project

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        database = Firebase.database.reference
        auth = Firebase.auth

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_search, R.id.nav_settings, R.id.nav_groups), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (auth.currentUser != null) {
            val user = auth.currentUser
            database.child("users").child(user!!.uid).get().addOnSuccessListener {
                if (it != null) {
                    val userData = it.value as Map<*, *>
                    navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_header_username).text = userData["username"].toString()
                    navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_header_email).text = userData["email"].toString()
                    setProfilePicture(navView.getHeaderView(0).findViewById(R.id.drawer_header_profile_pic))
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setProfilePicture(imageView: ImageView) {
        val storage = Firebase.storage.reference
        val auth = Firebase.auth
        val user = auth.currentUser

        database.child("users").child(user!!.uid).child("profile_pic").get()
            .addOnSuccessListener { pathToProfilePic ->

                val pathReference = storage.child(pathToProfilePic.value as String)
                val oneMegabyte: Long = 1024 * 1024

                pathReference.getBytes(oneMegabyte)
                    .addOnSuccessListener(this) {
                        val bitmap: Bitmap? = BitmapFactory.decodeByteArray(it, 0, it.size)
                        if (bitmap != null) imageView.setImageBitmap(bitmap)
                    } .addOnFailureListener(this) {
                        println("MainActivity setProfilePicture: Failed to download profile pic")
                    }
            }
            .addOnFailureListener {
                println("MainActivity setProfilePicture: Failed to get value users/uid/profile_pic")
            }
    }
}