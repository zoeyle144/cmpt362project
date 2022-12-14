package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.repositories.GroupsRepository

class GroupListViewModel: ViewModel(){
    private val repository = GroupsRepository()

    private val _groupsLiveData = MutableLiveData<List<Group>>()
    val groupsLiveData: LiveData<List<Group>> = _groupsLiveData

    fun getGroups(uid: String){
        repository.getGroups(_groupsLiveData, uid)
    }

    fun insert(group: Group){
        repository.insert(group)
    }

    fun delete(groupID:String){
        repository.delete(groupID)
    }
}

