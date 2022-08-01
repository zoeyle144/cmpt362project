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
//
//        taskEntry.setOnDragListener{v,e->
//            // Handles each of the expected events.
//            when (e.action) {
//                DragEvent.ACTION_DRAG_STARTED -> {
//                    // Determines if this View can accept the dragged data.
//                    if (e.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                        // As an example of what your application might do, applies a blue color tint
//                        // to the View to indicate that it can accept data.
//                        (v as? ImageView)?.setColorFilter(Color.BLUE)
//
//                        // Invalidate the view to force a redraw in the new tint.
//                        v.invalidate()
//
//                        // Returns true to indicate that the View can accept the dragged data.
//                        true
//                    } else {
//                        // Returns false to indicate that, during the current drag and drop operation,
//                        // this View will not receive events again until ACTION_DRAG_ENDED is sent.
//                        false
//                    }
//                }
//                DragEvent.ACTION_DRAG_ENTERED -> {
//                    // Applies a green tint to the View.
//                    (v as? ImageView)?.setColorFilter(Color.GREEN)
//
//                    // Invalidates the view to force a redraw in the new tint.
//                    v.invalidate()
//
//                    // Returns true; the value is ignored.
//                    true
//                }
//
//                DragEvent.ACTION_DRAG_LOCATION ->
//                    // Ignore the event.
//                    true
//                DragEvent.ACTION_DRAG_EXITED -> {
//                    // Resets the color tint to blue.
//                    (v as? ImageView)?.setColorFilter(Color.BLUE)
//
//                    // Invalidates the view to force a redraw in the new tint.
//                    v.invalidate()
//
//                    // Returns true; the value is ignored.
//                    true
//                }
//                DragEvent.ACTION_DROP -> {
//                    // Gets the item containing the dragged data.
//                    val item: ClipData.Item = e.clipData.getItemAt(0)
//
//                    // Gets the text data from the item.
//                    val dragData = item.text
//
//                    // Displays a message containing the dragged data.
//                    println("debug: Dragged data is $dragData")
//
//                    // Turns off any color tints.
//                    (v as? ImageView)?.clearColorFilter()
//
//                    // Invalidates the view to force a redraw.
//                    v.invalidate()
//
//                    // Returns true. DragEvent.getResult() will return true.
//                    true
//                }
//
//                DragEvent.ACTION_DRAG_ENDED -> {
//                    // Turns off any color tinting.
//                    (v as? ImageView)?.clearColorFilter()
//
//                    // Invalidates the view to force a redraw.
//                    v.invalidate()
//
//                    // Does a getResult(), and displays what happened.
//                    when(e.result) {
//                        true ->
//                            println("debug: The drop was handled")
//                        else ->
//                            println("debug: The drop didn't work")
//                    }
//                    // Returns true; the value is ignored.
//                    true
//                }
//                else -> {
//                    // An unknown action type was received.
//                    println("debug: Unknown action type received by View.OnDragListener.")
//                    false
//                }
//            }
//
//        }
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