package com.example.cmpt362project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cmpt362project.services.NotificationService
import com.example.cmpt362project.ui.settings.account.EmailDialogFragment
import com.example.cmpt362project.ui.settings.profile.SettingsProfileActivity
import com.example.cmpt362project.utility.ImageUtility
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var sharedPref: SharedPreferences

    private lateinit var drawerProfilePicView: ImageView
    private lateinit var pathToProfilePic: String

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        createNotificationChannel()
        val startIntent = Intent(applicationContext, NotificationService::class.java)
        startIntent.action = "Horizon Notification"
        startService(startIntent)

        database = Firebase.database.reference
        auth = Firebase.auth
        if (auth.currentUser != null) {
            user = auth.currentUser
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_search, R.id.nav_settings, R.id.nav_chats, R.id.nav_groups, R.id.nav_group_chats, R.id.nav_invitations), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val drawerUsernameView = navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_header_username)
        val drawerEmailView = navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_header_email)
        drawerProfilePicView = navView.getHeaderView(0).findViewById(R.id.drawer_header_profile_pic)
        viewModel.emailTextForDrawer.observe(this) { drawerEmailView.text = it }

        if (user != null) {
            database.child("users").child(user!!.uid).get().addOnSuccessListener {
                if (it != null) {
                    val userData = it.value as Map<*, *>
                    drawerUsernameView.text = userData["username"].toString()
                    viewModel.emailTextForDrawer.value = user!!.email
                    pathToProfilePic = userData["profilePic"].toString()
                    ImageUtility.setImageViewToProfilePic(pathToProfilePic, drawerProfilePicView)
                }
            }
        }

        // The user's profile picture has not changed recently on app start. Set it to false
        sharedPref = this.getSharedPreferences(SettingsProfileActivity.SHARED_PREF, Context.MODE_PRIVATE)
        sharedPref.registerOnSharedPreferenceChangeListener(this)
        setProfilePicRecentlyChangedFalse()
        setEmailRecentlyChangedFalse()

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPref.unregisterOnSharedPreferenceChangeListener(this)
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Check if the profile picture has changed recently. If it has, update the sidebar's PFP
        if (key == SettingsProfileActivity.KEY_PROFILE_PIC_RECENTLY_CHANGED) {
            val pfpRecentlyChanged = sharedPref.getBoolean(SettingsProfileActivity.KEY_PROFILE_PIC_RECENTLY_CHANGED, true)

            // The code setting KEY_PROFILE_PIC_RECENTLY_CHANGED to false calls this function again
            // Need to check if true to avoid calling setImageViewToProfilePic twice
            if (pfpRecentlyChanged && user != null) {
                database.child("users").child(user!!.uid).get().addOnSuccessListener {
                    if (it != null) {
                        val userData = it.value as Map<*, *>
                        pathToProfilePic = userData["profilePic"].toString()
                        ImageUtility.setImageViewToProfilePic(pathToProfilePic, drawerProfilePicView)
                        setProfilePicRecentlyChangedFalse()
                    }
                }
            }
        }
        if (key == EmailDialogFragment.KEY_EMAIL_RECENTLY_CHANGED) {
            println("onSharedPreferenceChanged EmailDialogFragment.KEY_EMAIL_RECENTLY_CHANGED true")
            val emailRecentlyChanged = sharedPref.getBoolean(EmailDialogFragment.KEY_EMAIL_RECENTLY_CHANGED, true)
            if (emailRecentlyChanged && user != null) {
                println("emailRecentlyChanged true")
                val newEmail = user!!.email
                viewModel.emailTextForDrawer.value = newEmail
                database.child("users").child(user!!.uid).child("email").setValue(newEmail)
                setEmailRecentlyChangedFalse()
                println("KEY_EMAIL_RECENTLY_CHANGED now ${sharedPref.getBoolean(EmailDialogFragment.KEY_EMAIL_RECENTLY_CHANGED, true)}")
            }
        }
    }

    private fun setProfilePicRecentlyChangedFalse() {
        with(sharedPref.edit()) {
            putBoolean(SettingsProfileActivity.KEY_PROFILE_PIC_RECENTLY_CHANGED, false)
            apply()
        }
    }

    private fun setEmailRecentlyChangedFalse() {
        with(sharedPref.edit()) {
            putBoolean(EmailDialogFragment.KEY_EMAIL_RECENTLY_CHANGED, false)
            apply()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Horizon Channel"
            val descriptionText = "Testing"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel-777", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
