package com.example.cmpt362project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.ChatListAdaptor
import com.example.cmpt362project.adaptors.MessageListAdaptor
import com.example.cmpt362project.models.Message
import com.example.cmpt362project.viewModels.ChatListViewModel
import com.example.cmpt362project.viewModels.MessageListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ChatConversationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversation)

        val chatId = intent.getStringExtra("chatId")
        var username = intent.getStringExtra("myUsername")
        var partnerName = intent.getStringExtra("otherUserUsername")
        val partnerNameView = findViewById<TextView>(R.id.conversationPartner)
        partnerNameView.text = partnerName
        val listView = findViewById<ListView>(R.id.msg_list_view)

        val adapter = MessageListAdaptor(this, listOf())
        listView.adapter = adapter


        val msgListViewModel: MessageListViewModel = ViewModelProvider(this)[MessageListViewModel::class.java]

        if (chatId != null) {
            Log.w("DEBUG", "fetched chat ${chatId}")
            msgListViewModel.fetchMessages(chatId)
        }
        msgListViewModel.msgListData.observe(this) { it ->
            adapter.clear()
            //Log.w("DEBUGR", it.toString())
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
            listView.setSelection(listView.adapter.count - 1)
        }



        var textEntry = findViewById<EditText>(R.id.msg_input_box)
        var submitBtn = findViewById<FloatingActionButton>(R.id.msg_send_btn)

        submitBtn.setOnClickListener {
            val text = textEntry.text.toString()
            if (text.isEmpty()) {

            } else {

                if (auth.currentUser != null && chatId != null && username != null) {
                    msgListViewModel.insert(Message(chatId, auth.currentUser!!.uid, username, text, System.currentTimeMillis()))
                }
                textEntry.setText("")
            }
        }
    }
}