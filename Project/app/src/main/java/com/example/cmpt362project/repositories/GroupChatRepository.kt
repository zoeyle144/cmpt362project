package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.GroupChat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupChatRepository {
    val database = Firebase.database
    val auth = Firebase.auth
    private val chatsRef = database.getReference("group_chats")

    fun fetchChats(liveData: MutableLiveData<List<GroupChat>>){
        chatsRef
            .orderByChild("lastUpdateTimestamp")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var chats: List<GroupChat> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(GroupChat::class.java)!!
                    }
                    Log.w("DEBUG", chats.toString())
                    //chats = chats.filter{it. == auth.currentUser!!.uid || it.user2 == auth.currentUser!!.uid}
                    liveData.postValue(chats.reversed())
                    Log.w("DEBUG", chats.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    fun insert(chat: GroupChat){

        chatsRef.get().addOnSuccessListener {
            var exists = false
            if (it.value != null) {
                val chatList = it.value as Map<*, *>
                for ((key, value) in chatList) {
                    var chatListEntry = value as Map<*, *>
                    if (chatListEntry["groupId"] == chat.groupId) {
                        exists = true
                        break
                    }
                }
            }
            if (!exists) {
                chatsRef.child(chat.chatId).setValue(chat)
                    .addOnCompleteListener{
                        println("debug: add group chat success")
                    }.addOnFailureListener { err ->
                        println("debug: add group chat fail Error ${err.message}")
                    }
            }

        }
    }

}