package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.models.TaskUpdateData
import com.example.cmpt362project.repositories.TasksRepository

class TaskListViewModel(): ViewModel(){
    private val repository = TasksRepository()

    private val _tasksLiveData = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun fetchTasks(groupID:String, boardID: String){
        repository.fetchTasks(_tasksLiveData,groupID, boardID)
    }

    fun insert(task: Task,groupID:String, boardID:String){
        repository.insert(task,groupID, boardID)
    }

    fun delete(groupID:String, boardID:String, id: String){
        repository.delete(groupID,boardID, id)
    }

    fun updateCategory(groupID:String, boardID: String, taskID:String, category:String){
        repository.updateCategory(groupID,boardID, taskID, category)
    }

    fun updateTask(groupID:String, boardID: String, task: TaskUpdateData){
        repository.updateTask(groupID,boardID, task)
    }
}