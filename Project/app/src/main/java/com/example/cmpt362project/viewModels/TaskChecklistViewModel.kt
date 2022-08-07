package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.TaskChecklistItem
import com.example.cmpt362project.repositories.TaskChecklistItemsRepository

class TaskChecklistViewModel(): ViewModel() {
    private val repository = TaskChecklistItemsRepository()

    private val _taskChecklistItemsLiveData = MutableLiveData<List<TaskChecklistItem>>()
    val taskChecklistItemsLiveData: LiveData<List<TaskChecklistItem>> = _taskChecklistItemsLiveData

    fun fetchChecklistItems(boardID: String, taskID: String){
        repository.fetchChecklistItems(_taskChecklistItemsLiveData, boardID, taskID)
    }

    fun insert(boardID:String, taskID: String, taskCheckListItem: TaskChecklistItem){
        repository.insert(boardID, taskID, taskCheckListItem)
    }

    fun delete(boardID:String, taskID: String, itemID: String){
        repository.delete(boardID, taskID, itemID)
    }

    fun updateCompleteField(boardID:String, taskID: String, itemID: String, isChecked:Boolean){
        repository.updateCompleteField(boardID, taskID, itemID, isChecked)
    }
}