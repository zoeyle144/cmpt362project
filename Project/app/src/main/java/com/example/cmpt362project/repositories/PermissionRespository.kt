package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.Permission
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

    fun getPermission(liveData: MutableLiveData<List<Permission>>, permissionID: String){
        permRef
            .child(permissionID)
            .child("permission")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val permission: List<Permission> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Permission::class.java)!!
                    }

                    liveData.postValue(permission)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun insert(permission: Permission){
        permRef.child(permission.permissionID).setValue(permission)
            .addOnCompleteListener{
                println("debug: add permission success")
            }.addOnFailureListener{ err ->
                println("debug: add permission fail Error ${err.message}")
            }
    }




//    fun delete(groupID: String){
//        permRef.child(groupID).removeValue()
//            .addOnSuccessListener {
//                println("debug: delete board success")
//            }.addOnFailureListener{ err ->
//                println("debug: delete board fail Error ${err.message}")
//            }
//    }

}