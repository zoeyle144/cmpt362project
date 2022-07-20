package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.repositories.TasksRepository

class TaskListViewModel: ViewModel(){
    private val repository = TasksRepository()

    private val _tasksLiveData = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun fetchTasks(){
        repository.fetchTasks(_tasksLiveData)
    }
}