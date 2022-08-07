package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.repositories.PermissionRespository
import com.example.cmpt362project.ui.groups.FirebaseSuccessListener

class PermissionViewModel: ViewModel() {
    private val repository = PermissionRespository()

    private val _permissionsLiveData = MutableLiveData<List<Permission>>()
    val permissionsLiveData: LiveData<List<Permission>> = _permissionsLiveData

    fun getPermission(permissionID: String){
        repository.getPermission(_permissionsLiveData, permissionID)
    }

    fun insert(permission: Permission){
        repository.insert(permission)
    }

    fun insertInGroup(permission: Permission, groupID: String) {
        repository.insertInGroup(permission, groupID)
    }

//    fun delete(permissionID:String){
//        repository.delete(permissionID)
//    }

    //given userID and groupID, need to find permission
    //search for groupID from Permission
    //then search for userID
    //then check if they're a match

    fun userHasRole(groupID: String, uID: String): String {
        return repository.userHasRole(groupID, uID)
    }

//    fun canDelete(groupID: String, uID: String): Boolean {
//        return repository.canDelete(groupID, uID)
//
//    }
//
//    fun canRead(groupID: String, uID: String): Boolean {
//        return repository.canRead(groupID, uID)
//
//    }
//
//    fun canEdit(groupID: String, uID: String): Boolean {
//        return repository.canEdit(groupID, uID)
//
//    }

    fun updateRole(groupID: String, uID: String, role: String) {
        return repository.updateRole(groupID, uID, role)
    }

}