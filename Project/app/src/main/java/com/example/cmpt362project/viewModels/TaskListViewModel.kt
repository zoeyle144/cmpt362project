package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.repositories.TasksRepository

class TaskListViewModel(): ViewModel(){
    private val repository = TasksRepository()

    private val _tasksLiveData = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun fetchTasks(boardID: String){
        repository.fetchTasks(_tasksLiveData, boardID)
    }

    fun insert(task: Task, boardID:String){
        repository.insert(task, boardID)
    }

    fun delete(boardID:String, id: String){
        repository.delete(boardID, id)
    }

    fun updateCategory(boardID: String, taskID:String, category:String){
        repository.updateCategory(boardID, taskID, category)
    }
}