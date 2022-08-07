package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.ChangeNotification
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChangeNotificationsRepository {
    val database = Firebase.database
    val auth = Firebase.auth
    private val changeNotificationsRef = database.getReference("changeNotifications")

    fun fetchChangeNotifications(liveData: MutableLiveData<List<ChangeNotification>>, groupID:String){
        changeNotificationsRef
            .orderByChild("groupID").equalTo(groupID)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks: List<ChangeNotification> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(ChangeNotification::class.java)!!
                    }
                    liveData.postValue(tasks)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}