package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.Permission
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
    val permRef = database.getReference("permission")

    fun getGroups(liveData: MutableLiveData<List<Group>>, uid:String){
        groupsRef
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val groups: List<Group> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Group::class.java)!!
                    }
                    var userGroupsIds = mutableListOf<String>()
                    var userGroups = mutableListOf<Group>()

                    permRef.get().addOnSuccessListener {
                        val permList = it.value as Map<*, *>
                        for ((key, value) in permList) {
                            var permListEntry = value as Map<*, *>
                            if (permListEntry["uid"].toString() == uid) {
                                userGroupsIds.add(permListEntry["groupID"].toString())
                            }
                        }
                        for (group in groups) {
                            if (userGroupsIds.contains(group.groupID)) {
                                userGroups.add(group)
                            }
                        }
                        liveData.postValue(userGroups)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun insert(group: Group){
        groupsRef.child(group.groupID).setValue(group)
            .addOnCompleteListener{
                println("debug: add group success")
            }.addOnFailureListener{ err ->
                println("debug: add group fail Error ${err.message}")
            }
    }


    fun delete(groupID: String){
        val groupChatsRef = database.getReference("group_chats")
        val msgRef = database.getReference("chat_messages")
        val invitationsRef = database.getReference("invitations")
        val permsRef = database.getReference("permission")
        groupsRef.child(groupID).removeValue()
            .addOnSuccessListener {
                println("debug: delete group success")
                msgRef.child(groupID).removeValue()
            }.addOnFailureListener{ err ->
                println("debug: delete group fail Error ${err.message}")
            }
    }

}
