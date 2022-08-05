package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.repositories.PermissionRespository

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

//    fun delete(permissionID:String){
//        repository.delete(permissionID)
//    }
}