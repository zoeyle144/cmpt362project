package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.ChatListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateChatActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_chat)

        val createChatButton = findViewById<Button>(R.id.create_chat_button)
        val userid1 = findViewById<TextView>(R.id.user1_uid_input)
        val userid2 = findViewById<EditText>(R.id.user2_uid_input)

        userid1.text = auth.currentUser?.uid
        userid2.setText("liHbzqfBStYNLjAqtNWb1C7szPj2")

        createChatButton.setOnClickListener{

            val chatListViewModel: ChatListViewModel = ViewModelProvider(this)[ChatListViewModel::class.java]
            val database = Firebase.database
            val chatsRef = database.getReference("chats")
            val chatID = chatsRef.push().key!!
            val user2uid = userid2.text.toString()

            val createdBy = auth.currentUser?.uid
            val chat = Chat(chatID, userid1.text as String, user2uid, System.currentTimeMillis())
            chatListViewModel.insert(chat)
            
            finish()

        }
    }
}