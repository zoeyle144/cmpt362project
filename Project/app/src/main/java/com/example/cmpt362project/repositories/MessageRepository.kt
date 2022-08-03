package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageRepository {
    val database = Firebase.database
    val auth = Firebase.auth
    private val msgRef = database.getReference("chat_messages")

    fun fetchMsgs(liveData: MutableLiveData<List<Message>>, chatId: String){
        msgRef.child(chatId)
            .orderByChild("timestamp")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages: List<Message> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Message::class.java)!!
                    }
                    //Log.w("DEBUG", messages.toString())
                    liveData.postValue(messages)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    fun insert(msg: Message){
        msgRef.child(msg.chatId).push().setValue(msg).addOnCompleteListener{
            println("debug: add message success")
        }.addOnFailureListener { err ->
            println("debug: add message fail Error ${err.message}")
        }

    }

}