package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.CategoryListAdaptor
import com.example.cmpt362project.adaptors.TaskListAdaptor
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayCategoryActivity: AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CategoryListAdaptor.ViewHolder>? = null
    private lateinit var categoryList: List<Category>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_category)

        val taskListViewModel: TaskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        taskListViewModel.fetchTasks()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        categoryList = ArrayList()
        adapter = CategoryListAdaptor(categoryList, taskListViewModel.tasksLiveData, this)
        recyclerView.adapter = adapter

        val categoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
        categoryListViewModel.fetchCategories()
        categoryListViewModel.categoriesLiveData.observe(this){
            (adapter as CategoryListAdaptor).updateList(it)
            (adapter as CategoryListAdaptor).notifyDataSetChanged()
        }

    }

    override fun onResume() {
        super.onResume()
        val taskListViewModel: TaskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        taskListViewModel.fetchTasks()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        categoryList = ArrayList()
        adapter = CategoryListAdaptor(categoryList, taskListViewModel.tasksLiveData, this)
        recyclerView.adapter = adapter

        val categoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
        categoryListViewModel.fetchCategories()
        categoryListViewModel.categoriesLiveData.observe(this){
            (adapter as CategoryListAdaptor).updateList(it)
            (adapter as CategoryListAdaptor).notifyDataSetChanged()
        }
    }
}