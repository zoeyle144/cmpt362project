package com.example.cmpt362project.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

                    var filteredPermissions = mutableListOf<Permission>()
                    for (perm in permission) {
                        if (perm.groupID == groupId) {
                            filteredPermissions.add(perm)
                        }
                    }
                    liveData.postValue(filteredPermissions)
                    //Log.w("DEBUGHERE", permission.toString())
                    //liveData.postValue(permission)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


    fun insert(permission: Permission) {
        permRef.child(permission.permissionID).setValue(permission)
            .addOnCompleteListener {
                println("debug: add permission success")
            }.addOnFailureListener { err ->
                println("debug: add permission fail Error ${err.message}")
            }
    }

    fun edit(permission: Permission) {
        permRef.child(permission.permissionID).setValue(permission)
    }

    fun delete(permission: Permission, uid: String){
        permRef.get().addOnSuccessListener {
            var hasPermission = false
            val permList = it.value as Map<*, *>
            for ((key, value) in permList) {
                var permListEntry = value as Map<*, *>
                Log.w("DEBUG", permListEntry.toString())
                if (permListEntry["uid"] == uid &&
                    permListEntry["groupID"] == permission.groupID &&
                    permListEntry["role"] == "admin"
                ) {
                    hasPermission = true
                    break
                }
            }
            if (hasPermission) {
                permRef.child(permission.permissionID).removeValue()
            }
        }

    }
}
//                println("debug: delete board success")
//            }.addOnFailureListener{ err ->
//                println("debug: delete board fail Error ${err.message}")
//            }
//    }



//    fun canDelete(groupID: String, uID: String): Boolean {
//        val allowed = arrayOf("author")
//
//        if (allowed.contains(userHasRole(groupID,uID))) {
//            println("Debug: yes delete permission")
//            return true
//        }
//        println("Debug: no delete permission")
//
//        return false
//    }
//
//    fun canEdit(groupID: String, uID: String): Boolean {
//        val allowed = arrayOf("author", "admin")
//        if (allowed.contains(userHasRole(groupID,uID))) {
//            println("Debug: yes edit permission")
//
//            return true
//        }
//        println("Debug: no edit permission")
//
//        return false
//    }
//
//    fun canRead(groupID: String, uID: String): Boolean {
//        val allowed = arrayOf("author", "admin", "reader", "moderator")
//
//        if (allowed.contains(userHasRole(groupID,uID))) {
//            println("Debug: yes read permission")
//            return true
//        }
//        println("Debug: no read permission")
//        return false
//    }
//
//    fun canInvite(groupID: String, uID: String): Boolean {
//        val allowed = arrayOf("admin", "moderator")
//        val ans = userHasRole(groupID, uID)
//
//        println("Debug:ans  $ans")
//        if (allowed.contains(userHasRole(groupID,uID))) {
//            println("Debug: yes add permission")
//            return true
//        }
//        println("Debug: no add permission")
//        return false
//    }

//    fun searchUserInGroup(groupID: String, uID: String, firebaseSuccessListener: FirebaseSuccessListener): Boolean {
////        permRef.orderByChild("groupID").equalTo(groupID)
////            .orderByChild("userID").equalTo(uID)
//        val user = "$groupID _ $uID"
//        var result: Boolean = false
//        permRef
//            .orderByChild("groupID_uID")
//            .equalTo(user)
//            .addListenerForSingleValueEvent(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if(snapshot.exists()){
//                        //user has some permission
//                        firebaseSuccessListener.setSearchResult(true)
//                    } else {
//                        //doesn't exists
//                        firebaseSuccessListener.setSearchResult(false)
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        return result
//    }
/*
    fun updateRole(groupID:String, uID: String, role: String) {
        println("Debug: Updated role ${getPermissionID()}")
        permRef
//            .orderByChild("groupID_uID")
//            .equalTo(user)
            .child(getPermissionID())
            .child("role")
            .setValue(role)
    }

//    fun getPermissionID (groupID:String, uID: String, firebaseSuccessListener: FirebaseSuccessListener) {
//        val user = "$groupID _ $uID"
//        permRef
//            .orderByChild("groupID_uID")
//            .equalTo(user)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        //user has some permission
//                        val ans = snapshot.child("permissionID").getValue().toString()
//                        firebaseSuccessListener.setPermissionID(ans)
//                    } else {
//                        //doesn't exists
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })
//    }


    fun userHasRole(groupID:String, uID: String): String {
        var roleResult = ""
            permRef
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {

                            roleResult = i.child("role").getValue().toString()

                            if((i.child("groupID").getValue().toString().equals(groupID)) &&
                                (i.child("uid").getValue().toString().equals(uID))) {

                                roleResult = i.child("role").getValue().toString()

                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        println("Debug: role $roleResult")
        return roleResult
    }

    private var searchResult: Boolean = false
    private var permissionID: String = ""
    private var roleResult = ""

    

    fun getSearchResult (): Boolean {
        println("Debug: zoey ")

        return searchResult
    }

    fun getPermissionID (): String {
        return permissionID
    }

    fun getUserRole (): String {
        return roleResult
    }


//    override fun setSearchResult(existed: Boolean) {
//        searchResult = existed
//    }
//
//    override fun setPermissionID(pID: String) {
//        println("Debug: SET PERMISSION")
//            permissionID = pID
//    }
//
//    override fun setUserRole(role: String) {
//        println("Debug: SET roleResult")
//        roleResult = role
//    }

    fun setSearchResult(ans: Boolean){
        println("Debug: hello")
        searchResult = ans
    }


    fun searchUserInGroup(groupID:String, uID: String): Boolean {
        val user = "$groupID _ $uID"
        var result: Boolean = false
        permRef
            .orderByChild("groupID_uID")
            .equalTo(user)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        //user has some permission
                        setSearchResult(true)
                        println("Debug: res inside $result")

                    } else {
                        //doesn't exists
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        result = getSearchResult()
        println("Debug: res $result")
        return result
    }

}
*/