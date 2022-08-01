package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Group
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupsRepository {
    val database = Firebase.database
    val auth = Firebase.auth
    val groupsRef = database.getReference("groups")

    fun getGroups(liveData: MutableLiveData<List<Group>>){
        groupsRef
            .orderByChild("createdBy").equalTo(auth.currentUser?.uid.toString())
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val groups: List<Group> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Group::class.java)!!
                    }

                    liveData.postValue(groups)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}
