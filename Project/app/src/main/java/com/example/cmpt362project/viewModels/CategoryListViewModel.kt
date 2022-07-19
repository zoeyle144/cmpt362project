package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.repositories.CategoriesRepository

class CategoryListViewModel: ViewModel(){
    private val repository = CategoriesRepository()

    private val _categoriesLiveData = MutableLiveData<List<Category>>()
    val categoriesLiveData: LiveData<List<Category>> = _categoriesLiveData

    fun fetchCategories(){
        repository.fetchCategories(_categoriesLiveData)
    }
}