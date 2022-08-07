package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Invitation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InvitationRepository {
    val database = Firebase.database
    val auth = Firebase.auth
    private val invitationsRef= database.getReference("invitations")
    private val permissionRef= database.getReference("permission")

    fun fetchInvitations(liveData: MutableLiveData<List<Invitation>>){
        invitationsRef
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var invitations: List<Invitation> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Invitation::class.java)!!
                    }
                    invitations = invitations.filter{it.receiver == auth.currentUser!!.uid}
                    liveData.postValue(invitations.reversed())
                    Log.w("DEBUG", invitations.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    fun insert(invitation: Invitation){

        invitationsRef.get().addOnSuccessListener {
            var exists = false
            if (it.value != null) {
                val invitationList = it.value as Map<*, *>
                for ((key, value) in invitationList) {
                    var invitationListEntry = value as Map<*, *>
                    if (invitationListEntry["sender"] == invitation.sender && invitationListEntry["receiver"] == invitation.receiver && invitationListEntry["groupID"] == invitation.groupId) {
                        exists = true
                        break
                    }
                }
            }
            if (!exists) {
                invitationsRef.child(invitation.invitationId).setValue(invitation)
                    .addOnCompleteListener{
                        println("debug: add invitation success")
                    }.addOnFailureListener { err ->
                        println("debug: add invitation fail Error ${err.message}")
                    }
            }

        }
    }

    fun delete(invitation_id: String) {
        invitationsRef.child(invitation_id).removeValue().addOnSuccessListener {
            println("delete Success")
        }
    }
}