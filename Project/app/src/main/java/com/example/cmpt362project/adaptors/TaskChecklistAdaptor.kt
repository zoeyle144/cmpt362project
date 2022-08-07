package com.example.cmpt362project.adaptors

import android.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.TaskChecklistItem
import com.example.cmpt362project.viewModels.TaskChecklistViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TaskChecklistAdaptor(private var taskChecklistItemList: List<TaskChecklistItem>, private var boardID:String, private var taskID:String) : RecyclerView.Adapter<TaskChecklistAdaptor.ViewHolder>(){
    private lateinit var  taskChecklistViewModel: TaskChecklistViewModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskChecklistAdaptor.ViewHolder {
        if(viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_checklist_adaptor, parent, false)
            return ViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_task_checklist_item_button, parent, false)
            val addTaskChecklistItemButton= view.findViewById<Button>(R.id.add_task_checklist_item_button)

            if (MainActivity.role == "reader"){
                addTaskChecklistItemButton.isEnabled = false
            }

            addTaskChecklistItemButton.setOnClickListener{
                val builder = AlertDialog.Builder(parent.context)
                val input = EditText(parent.context)
                input.setRawInputType(InputType.TYPE_CLASS_TEXT)
                builder.setView(input)
                    .setTitle("Checklist Item")
                    .setPositiveButton("OK", DialogInterface.OnClickListener{ dialog, _ ->
                        val database = Firebase.database
                        val boardsRef = database.getReference("boards")
                        val auth = Firebase.auth
                        val checklistItemName = input.text.toString()
                        taskChecklistViewModel = ViewModelProvider(parent.context as ViewModelStoreOwner)[TaskChecklistViewModel::class.java]
                        val taskChecklistItemID = boardsRef.child(boardID).child("tasks").child(taskID).child("checklist").push().key!!
                        val createdBy = auth.currentUser?.uid
                        val complete = false
                        val taskChecklistItem = TaskChecklistItem(taskChecklistItemID, checklistItemName, createdBy!!, complete)
                        taskChecklistViewModel.insert(boardID, taskID, taskChecklistItem)
                        dialog.dismiss()
                    })
                    .setNegativeButton("CANCEL", DialogInterface.OnClickListener{ dialog, _ ->
                        dialog.dismiss()
                    })
                builder.create()
                builder.show()
            }

            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: TaskChecklistAdaptor.ViewHolder, position: Int) {
        if (position < itemCount-1) {
            val taskCheckListItemName =
                holder.itemView.findViewById<TextView>(R.id.task_checklist_item_name)
            val taskCheckListItemID =
                holder.itemView.findViewById<TextView>(R.id.task_checklist_item_id)
            val taskBoardID = holder.itemView.findViewById<TextView>(R.id.task_board_id)
            val checklistTaskID = holder.itemView.findViewById<TextView>(R.id.task_id)
            val checkbox = holder.itemView.findViewById<CheckBox>(R.id.checlist_item_checkbox)
            taskCheckListItemName.text = taskChecklistItemList[position].name
            taskCheckListItemID.text = taskChecklistItemList[position].taskChecklistItemID
            taskBoardID.text = boardID
            checklistTaskID.text = taskID
            checkbox.isChecked = taskChecklistItemList[position].complete

            taskChecklistViewModel = ViewModelProvider(holder.itemView.context as ViewModelStoreOwner)[TaskChecklistViewModel::class.java]

            checkbox.setOnClickListener{
                taskChecklistViewModel.updateCompleteField(boardID, taskID, taskChecklistItemList[position].taskChecklistItemID, checkbox.isChecked)
            }


            val deleteButton = holder.itemView.findViewById<ImageView>(R.id.checlist_item_delete)
            deleteButton.setOnClickListener{
                taskChecklistViewModel.delete(boardID, taskID, taskChecklistItemList[position].taskChecklistItemID)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskChecklistItemList.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemCount-1){
            0
        }else{
            1
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    fun updateList(newList:List<TaskChecklistItem>){
        taskChecklistItemList = newList
    }

}