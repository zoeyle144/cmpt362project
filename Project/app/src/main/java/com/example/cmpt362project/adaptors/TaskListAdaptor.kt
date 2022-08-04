package com.example.cmpt362project.adaptors

import android.R.id
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayTaskActivity
import com.example.cmpt362project.models.Task


class TaskListAdaptor(private var taskList: List<Task>, private var boardID:String) : RecyclerView.Adapter<TaskListAdaptor.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdaptor.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_adaptor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListAdaptor.ViewHolder, position: Int) {

        val taskEntryTitle = holder.itemView.findViewById<TextView>(R.id.task_entry_title)
        val taskID = holder.itemView.findViewById<TextView>(R.id.task_id)
        val taskBoardID = holder.itemView.findViewById<TextView>(R.id.task_board_id)
        taskEntryTitle.text = taskList[position].name
        taskID.text = taskList[position].taskID
        taskBoardID.text = boardID
        val taskEntry = holder.itemView.findViewById<CardView>(R.id.task_entry)
        taskEntry.setOnClickListener {
            val intent = Intent(holder.itemView.context, DisplayTaskActivity::class.java)
            intent.putExtra("task", taskList[position])
            intent.putExtra("boardID", boardID)
            holder.itemView.context.startActivity(intent)
        }

        taskEntry.setOnLongClickListener{
            val clipText = " This is taskEntry"
            val item = ClipData.Item(clipText)
            val dragData = ClipData(clipText, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val myshadow = View.DragShadowBuilder(it)
            it.startDragAndDrop(dragData, myshadow, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    fun updateList(newList:List<Task>){
        taskList = newList
    }
}