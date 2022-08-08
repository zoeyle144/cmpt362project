package com.example.cmpt362project.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.ChatConversationActivity
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.utility.ImageUtility
import com.example.cmpt362project.viewModels.ChatListViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchUserResultActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH_USER_RESULT_USERNAME = "KEY_SEARCH_USER_RESULT_USERNAME"
        const val KEY_SEARCH_USER_RESULT_EMAIL = "KEY_SEARCH_USER_RESULT_EMAIL"
        const val KEY_SEARCH_USER_RESULT_NAME = "KEY_SEARCH_USER_RESULT_NAME"
        const val KEY_SEARCH_USER_RESULT_PROFILE_PIC = "KEY_SEARCH_USER_RESULT_PROFILE_PIC"
        const val KEY_SEARCH_USER_RESULT_ABOUT_ME = "KEY_SEARCH_USER_RESULT_ABOUT_ME"
    }

    var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_profile)

        val toolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_baseline_close_24, theme))
        supportActionBar?.setHomeActionContentDescription(getString(R.string.profile_toolbar_close))

        var email = ""
        var name = ""
        var profilePic = ""
        var aboutMe = ""

        val usernameView = findViewById<TextInputLayout>(R.id.profile_username_field)
        val emailView = findViewById<TextInputLayout>(R.id.profile_email_field)
        val nameView = findViewById<TextInputLayout>(R.id.profile_name_field)
        val pictureView = findViewById<ImageView>(R.id.profile_picture)
        val aboutMeView = findViewById<TextInputLayout>(R.id.profile_about_me_field)

        val extras = intent.extras
        if (extras != null) {
            username = extras.getString(KEY_SEARCH_USER_RESULT_USERNAME, "")
            email = extras.getString(KEY_SEARCH_USER_RESULT_EMAIL, "")
            name = extras.getString(KEY_SEARCH_USER_RESULT_NAME, "")
            profilePic = extras.getString(KEY_SEARCH_USER_RESULT_PROFILE_PIC, "")
            aboutMe = extras.getString(KEY_SEARCH_USER_RESULT_ABOUT_ME, "")

            supportActionBar?.title = "$username's Profile"

        }

        usernameView.editText?.setText(username)
        emailView.editText?.setText(email)
        nameView.editText?.setText(name)
        ImageUtility.setImageViewToProfilePic(profilePic, pictureView)
        aboutMeView.editText?.setText(aboutMe)

        // Disable functionality of the R.layout.activity_settings_profile layout
        usernameView.isEnabled = false
        emailView.isEnabled = false
        nameView.isEnabled = false
        aboutMeView.isEnabled = false
        aboutMeView.isCounterEnabled = false

        val changePictureButton = findViewById<MaterialButton>(R.id.profile_picture_change_picture_button)
        changePictureButton.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_search_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.profile_search_toolbar_chat -> {
            val chatListViewModel: ChatListViewModel = ViewModelProvider(this)[ChatListViewModel::class.java]
            val database = Firebase.database
            val auth = Firebase.auth
            val chatsRef = database.getReference("chats")
            val usernamesRef = database.getReference("usernames")
            val usersRef = database.getReference("users")
            val chatID = chatsRef.push().key!!
            val user1uid = auth.uid
            var user2uid = ""
            var myUsername = ""

            Log.w("DEBUGU", username)
            usernamesRef.child(username).get().addOnSuccessListener {
                Log.w("DEBUGZ", it.value.toString())
                user2uid = it.value.toString()
                usersRef.child(auth.currentUser!!.uid).get().addOnSuccessListener {
                    var usersListEntry = it.value as Map<*, *>
                    myUsername = usersListEntry["username"].toString()

                    val chat = Chat(chatID, user1uid as String, user2uid, System.currentTimeMillis())
                    var actualChatID = chatID

                    chatsRef.get().addOnSuccessListener {
                        var exists = false
                        if (it.value != null) {
                            val chatList = it.value as Map<*, *>
                            for ((key, value) in chatList) {
                                var chatListEntry = value as Map<*, *>
                                if (chatListEntry["user1"] == chat.user1 && chatListEntry["user2"] == chat.user2
                                    || chatListEntry["user2"] == chat.user1 && chatListEntry["user1"] == chat.user2
                                ) {
                                    Log.w("DEBUGT", chatListEntry["chatId"].toString())
                                    actualChatID = chatListEntry["chatId"].toString()
                                    break
                                }
                            }

                        }
                        if (!exists) {
                            chatListViewModel.insert(Chat(actualChatID, user1uid, user2uid, System.currentTimeMillis()))
                        }


                        val intent = Intent(this, ChatConversationActivity::class.java)
                        intent.putExtra("chatId", actualChatID)
                        intent.putExtra("otherUser", user2uid)
                        intent.putExtra("otherUserUsername", username)
                        intent.putExtra("myUsername", myUsername)
                        startActivity(intent)
                    }
                }
            }

            true
        }
        R.id.profile_search_toolbar_invite -> {

            true
        }
        else -> {
            println("Click close")
            finish()
            true
        }
    }
}