package com.example.cmpt362project.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateCategoryActivity
import com.example.cmpt362project.activities.CreateTaskActivity
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class CategoryListAdaptor(private var categoryList: List<Category>, private var taskLiveData: LiveData<List<Task>>, private var boardTitle:String, private var lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<CategoryListAdaptor.ViewHolder>(){

    private lateinit var taskListAdaptor: TaskListAdaptor
    private lateinit var taskList: List<Task>
    private lateinit var taskListView: ListView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListAdaptor.ViewHolder {
        if(viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.category_list_adaptor, parent, false)
            return ViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_category_button, parent, false)
            val addCategoryButton = view.findViewById<Button>(R.id.add_category_button)

            addCategoryButton.setOnClickListener {
                val intent = Intent(view.context, CreateCategoryActivity::class.java)
                intent.putExtra("boardTitle", boardTitle)
                view.context.startActivity(intent)
            }

            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CategoryListAdaptor.ViewHolder, position: Int) {
        if (position < itemCount-1){
            holder.itemTitle?.text = categoryList[position].title
            val categoryTitle = categoryList[position].title
            taskListView =holder.itemView.findViewById(R.id.task_list)
            taskList =  ArrayList()
            taskListAdaptor = TaskListAdaptor(holder.itemView.context, taskList)
            taskListView.adapter = taskListAdaptor
            taskListView.divider = null

            taskLiveData.observe(lifecycleOwner){
                val mutableIt = it.toMutableList()
                mutableIt.removeIf{ it -> it.category != categoryTitle}
                val mutableList = mutableIt.toList()
                taskListAdaptor.updateList(mutableList)
                taskListAdaptor.notifyDataSetChanged()
            }

            val addTaskButton = holder.itemView.findViewById<Button>(R.id.add_task_button)
            addTaskButton.setOnClickListener {
                val intent = Intent(holder.itemView.context, CreateTaskActivity::class.java)
                intent.putExtra("category_title", categoryTitle)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemCount-1){
            0
        }else{
            1
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView? = itemView.findViewById(R.id.item_title)
    }

    fun updateList(newList:List<Category>){
        categoryList = newList
    }
}