package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.repositories.CategoriesRepository
import com.example.cmpt362project.repositories.TasksRepository

class CategoryListViewModel: ViewModel(){
    private val repository = CategoriesRepository()
    private val repository2 = TasksRepository()

    private val _categoriesLiveData = MutableLiveData<List<Category>>()
    val categoriesLiveData: LiveData<List<Category>> = _categoriesLiveData

    fun fetchCategories(boardID: String){
        repository.fetchCategories(_categoriesLiveData, boardID)
    }

    fun insert(category: Category, boardID:String){
        repository.insert(category, boardID)
    }

    fun delete(boardID: String, categoryID:String, correspondTaskList:MutableList<String>){
        val iterator = correspondTaskList.listIterator()
        for (i in iterator) {
            repository2.delete(boardID, i)
        }
        repository.delete(boardID, categoryID)
    }
}