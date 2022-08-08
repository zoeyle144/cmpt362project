package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.ui.groups.FirebaseSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PermissionRespository {
    val database = Firebase.database
    val auth = Firebase.auth
    val permRef = database.getReference("permission")

    fun getPermissions(liveData: MutableLiveData<List<Permission>>, groupId: String) {
        permRef
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val permission: List<Permission> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Permission::class.java)!!
                    }
                    Log.w("DEBUGHERE", permission.toString())
                    var filteredPermissions = mutableListOf<Permission>()
                    for (perm in permission) {
                        if (perm.groupID == groupId) {
                            filteredPermissions.add(perm)
                            Log.w("DEBUGHERE", perm.toString())
                        }
                    }
                    liveData.postValue(filteredPermissions)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


    fun insert(permission: Permission) {
        permRef.get().addOnSuccessListener {
            var exists = false
            if (it.value == null) {
                permRef.child(permission.permissionID).setValue(permission)
                    .addOnCompleteListener {

                    }.addOnFailureListener { err ->
                        println("debug: add permission fail Error ${err.message}")
                    }

            }
            else {
                val permList = it.value as Map<*, *>
                for ((key, value) in permList) {
                    var permListEntry = value as Map<*, *>
                    Log.w("DEBUG", permListEntry.toString())
                    if (permListEntry["uid"] == permission.uid &&
                        permListEntry["groupID"] == permission.groupID
                    ) {
                        exists = true
                        break
                    }
                }
                if (!exists) {
                    permRef.child(permission.permissionID).setValue(permission)
                        .addOnCompleteListener {
                            println("debug: add permission success")
                        }.addOnFailureListener { err ->
                            println("debug: add permission fail Error ${err.message}")
                        }
                }
            }

        }
    }

    fun replace(permissionId: String, role: String) {
        permRef.child(permissionId).child("role").setValue(role)
    }

    fun delete(permission: Permission, uid: String){
        permRef.get().addOnSuccessListener {
            var hasPermission = false
            val permList = it.value as Map<*, *>
            for ((key, value) in permList) {
                var permListEntry = value as Map<*, *>
                Log.w("DEBUG", permListEntry.toString())

                // you can remove self from group
                // or you are an admin of that group and can remove others
                if (permListEntry["uid"] == uid ||
                    (permListEntry["uid"] == uid &&
                        permListEntry["groupID"] == permission.groupID &&
                        permListEntry["role"] == "admin"
                            )
                ) {
                    hasPermission = true
                    break
                }
            }
            if (hasPermission) {
                permRef.child(permission.permissionID).removeValue()
            }
        }

        // refresh groups
        val groupsRef = database.getReference("groups")
        val refresherName = "refresher"
        val description = "used to refresh groupList if permissions change"
        groupsRef.child(refresherName).setValue(Group(refresherName, System.currentTimeMillis().toString(), description))

        // refresh boards
        val boardsRef = database.getReference("boards")
        val refresherNameBoard = "refresher"
        val descriptionBoard = "used to refresh boardList if permissions change"
        boardsRef.child(refresherNameBoard).setValue(Board(refresherNameBoard, System.currentTimeMillis().toString(), "", "", descriptionBoard, ""))

    }
}