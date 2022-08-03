package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRepository {
    val database = Firebase.database
    val auth = Firebase.auth
    private val chatsRef = database.getReference("chats")

    fun fetchChats(liveData: MutableLiveData<List<Chat>>){
        chatsRef
            .orderByChild("lastUpdateTimestamp")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chats: List<Chat> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Chat::class.java)!!
                    }

                    liveData.postValue(chats.reversed())
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    fun insert(chat: Chat){

        chatsRef.get().addOnSuccessListener {
            var exists = false
            if (it.value != null) {
                val chatList = it.value as Map<*, *>
                for ((key, value) in chatList) {
                    var chatListEntry = value as Map<*, *>
                    if (chatListEntry["user1"] == chat.user1 && chatListEntry["user2"] == chat.user2
                        || chatListEntry["user2"] == chat.user1 && chatListEntry["user1"]  == chat.user2) {
                        exists = true
                        break
                    }
                }
            }
            if (!exists) {
                chatsRef.child(chat.chatId).setValue(chat)
                    .addOnCompleteListener{
                        println("debug: add chat success")
                    }.addOnFailureListener { err ->
                        println("debug: add cat fail Error ${err.message}")
                    }
            }

        }
    }

}