package com.example.cmpt362project.adaptors

import android.R.id
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Color
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayTaskActivity
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.models.TaskChecklistItem
import com.example.cmpt362project.viewModels.TaskChecklistViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel


class TaskListAdaptor(private var taskList: List<Task>, private var boardID:String, private var groupID:String) : RecyclerView.Adapter<TaskListAdaptor.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdaptor.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_adaptor, parent, false)
        val lifecycleOwner = parent.context as LifecycleOwner
        return ViewHolder(view, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: TaskListAdaptor.ViewHolder, position: Int) {
        val taskEntryTitle = holder.itemView.findViewById<TextView>(R.id.task_entry_title)
        val taskID = holder.itemView.findViewById<TextView>(R.id.task_id)
        val taskBoardID = holder.itemView.findViewById<TextView>(R.id.task_board_id)
        taskEntryTitle.text = taskList[position].name
        taskID.text = taskList[position].taskID
        taskBoardID.text = boardID
        val taskEntry = holder.itemView.findViewById<CardView>(R.id.task_entry)

        if (taskList[position].type == "Easy"){
            taskEntry.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }else if (taskList[position].type == "Medium"){
            taskEntry.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
        }else{
            taskEntry.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }

        taskEntry.setOnClickListener {
            val intent = Intent(holder.itemView.context, DisplayTaskActivity::class.java)
            intent.putExtra("task", taskList[position])
            intent.putExtra("boardID", boardID)
            intent.putExtra("groupID", groupID)
            holder.itemView.context.startActivity(intent)
        }

        taskEntry.setOnLongClickListener{
//            if (MainActivity.role == "reader"){
//                Toast.makeText(holder.itemView.context,
//                    "You do not have permission to drag and drop tasks",
//                    Toast.LENGTH_SHORT).show()
//                false
//            }else{
                val clipText = "This is taskEntry"
                val item = ClipData.Item(clipText)
                val dragData = ClipData(clipText, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
                val myshadow = View.DragShadowBuilder(it)
                it.startDragAndDrop(dragData, myshadow, it, 0)
                it.visibility = View.INVISIBLE
                true
//            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView: View, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder(itemView){
    }

    fun updateList(newList:List<Task>){
        taskList = newList
    }
}