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

    fun getPermissions(permissionID: String){
        repository.getPermissions(_permissionsLiveData, permissionID)
    }

    fun insert(permission: Permission){
        repository.insert(permission)
    }

    fun replace(permissionId: String, role: String) {
        repository.replace(permissionId, role)
    }

    fun delete(permission:Permission, uid:String){
        repository.delete(permission, uid)
    }



}