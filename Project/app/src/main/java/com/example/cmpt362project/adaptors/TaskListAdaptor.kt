package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayTaskActivity
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import org.w3c.dom.Text

class TaskListAdaptor(val context: Context, private var taskList: List<Task>): BaseAdapter() {

    override fun getCount(): Int {
        return taskList.size
    }

    override fun getItem(p0: Int): Any {
        return taskList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.task_list_adaptor, null)
        val taskEntryTitle = view.findViewById<TextView>(R.id.task_entry_title)
        taskEntryTitle.text = taskList[p0].name
        val taskEntry = view.findViewById<CardView>(R.id.task_entry)
        taskEntry.setOnClickListener {
            val intent = Intent(view.context, DisplayTaskActivity::class.java)
            intent.putExtra("task", taskList[p0])
            view.context.startActivity(intent)
        }
        return view
    }

    fun updateList(newList:List<Task>){
        taskList = newList
    }

}