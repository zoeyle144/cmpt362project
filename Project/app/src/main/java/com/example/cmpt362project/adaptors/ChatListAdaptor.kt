package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.ChatConversationActivity
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListAdaptor(val context: Context, private var chatList: List<Chat>): BaseAdapter() {
    val database = Firebase.database
    val auth = Firebase.auth

    override fun getCount(): Int {
        return chatList.size
    }

    override fun getItem(p0: Int): Any {
        return chatList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.chat_list_adaptor, null)
        val chatEntry = view.findViewById<Button>(R.id.chat_entry)

        var otherUserUid = chatList[p0].user2
        if (auth.currentUser?.uid == otherUserUid) {
            otherUserUid = chatList[p0].user1
        }

        var username = ""
        var otherUsername = ""
        database.getReference("users").child(otherUserUid).get().addOnSuccessListener {
            if (it.value != null) {
                var chatListEntry = it.value as Map<*, *>
                otherUsername = chatListEntry["username"].toString()
                chatEntry.text = otherUsername
            }
        }.addOnFailureListener() {
            chatEntry.text = "ERROR"
        }

        database.getReference("users").child(auth.currentUser!!.uid).get().addOnSuccessListener {
            var chatListEntry = it.value as Map<*, *>
            username = chatListEntry["username"].toString()
            chatEntry.text = otherUsername
        }.addOnFailureListener() {
            chatEntry.text = "ERROR"
        }

        chatEntry.setOnClickListener {
            val intent = Intent(view.context, ChatConversationActivity::class.java)
            intent.putExtra("chatId", chatList[p0].chatId)
            intent.putExtra("otherUser", otherUserUid)
            intent.putExtra("otherUserUsername", otherUsername)
            intent.putExtra("myUsername", username)
            view.context.startActivity(intent)
        }
        return view
    }

    fun updateList(newList:List<Chat>){
        chatList = newList
    }

    fun clear() {
        chatList = listOf()
    }
}