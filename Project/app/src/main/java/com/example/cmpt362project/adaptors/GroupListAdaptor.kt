package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.ChatConversationActivity
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.activities.DisplayGroupActivity
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.GroupChat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupListAdaptor(val context: Context, private var groupList: List<Group>): BaseAdapter() {
    val database = Firebase.database
    val auth = Firebase.auth

    override fun getCount(): Int {
        return groupList.size
    }

    override fun getItem(p0: Int): Any {
        return groupList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.group_list_adapter, null)
        val groupEntry = view.findViewById<Button>(R.id.group_chat_entry)

        var groupID  = groupList[p0].groupID
        var groupName = groupList[p0].groupName
        var groupDescription = groupList[p0].description

        groupEntry.text = groupName
        groupEntry.setOnClickListener{
            val intent = Intent(view.context, DisplayGroupActivity::class.java)
            intent.putExtra("groupID", groupID)
            intent.putExtra("groupName", groupName)
            intent.putExtra("groupDescription", groupDescription)
            view.context.startActivity(intent)
        }
        /*
        database.getReference("groups").child(groupId).get().addOnSuccessListener {
            if (it.value != null) {
                var groupData = it.value as Map<*, *>
                Log.w("DEBUG", groupData.toString())
                groupName = groupData["groupName"].toString()
                chatEntry.text = groupName

                database.getReference("users").child(auth.currentUser!!.uid).get().addOnSuccessListener {
                    if (it.value != null) {
                        var userData = it.value as Map<*, *>
                        username = userData["username"].toString()

                        chatEntry.setOnClickListener {
                            val intent = Intent(view.context, ChatConversationActivity::class.java)
                            intent.putExtra("chatId", chatList[p0].chatId)
                            intent.putExtra("otherUser", groupId)
                            intent.putExtra("otherUserUsername", groupName)
                            intent.putExtra("myUsername", username)
                            view.context.startActivity(intent)
                        }
                    }
                }.addOnFailureListener() {
                    chatEntry.text = "ERROR"
                }
            }
        }

         */

        return view
}

    fun updateList(newList:List<Group>){
        groupList = newList
    }

    fun clear() {
        groupList = listOf()
    }
}