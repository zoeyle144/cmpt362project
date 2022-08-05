package com.example.cmpt362project.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.BoardDragManageAdaptor
import com.example.cmpt362project.adaptors.BoardListAdaptor
import com.example.cmpt362project.adaptors.TaskChecklistAdaptor
import com.example.cmpt362project.adaptors.TaskChecklistDragManageAdaptor
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.models.TaskChecklistItem
import com.example.cmpt362project.models.TaskUpdateData
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskChecklistViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayTaskActivity : AppCompatActivity() {
    private lateinit var taskListViewModel: TaskListViewModel
    private lateinit var taskChecklistViewModel: TaskChecklistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_task)

        val boardID = intent.getSerializableExtra("boardID").toString()

        val t = intent.getParcelableExtra<Task>("task")
        val taskName = findViewById<EditText>(R.id.task_name)
        val taskSummary = findViewById<EditText>(R.id.task_summary)
        val taskType = findViewById<EditText>(R.id.task_type)
        val taskStartDate = findViewById<EditText>(R.id.task_start_date)
        val taskEndDate = findViewById<EditText>(R.id.task_end_date)

        val saveButton = findViewById<Button>(R.id.save_display_task)
        val closeButton = findViewById<Button>(R.id.close_display_task)

        taskName.setText(t?.name)
        taskSummary.setText(t?.summary)
        taskType.setText(t?.type)
        taskStartDate.setText(t?.startDate)
        taskEndDate.setText(t?.endDate)

        val taskID = t?.taskID.toString()

        val taskChecklistView: RecyclerView = findViewById(R.id.task_checklist)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        taskChecklistView.layoutManager = layoutManager
        val taskChecklistItemList: List<TaskChecklistItem> = ArrayList()
        val adapter: RecyclerView.Adapter<TaskChecklistAdaptor.ViewHolder> = TaskChecklistAdaptor(taskChecklistItemList, boardID, taskID)
        taskChecklistView.adapter = adapter
        taskChecklistView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        taskChecklistViewModel = ViewModelProvider(this)[TaskChecklistViewModel::class.java]
        taskChecklistViewModel.fetchTasks(boardID, taskID)
        taskChecklistViewModel.taskChecklistItemsLiveData.observe(this){
            (adapter as TaskChecklistAdaptor).updateList(it)
            (adapter as TaskChecklistAdaptor).notifyDataSetChanged()
        }

        val dividerItemDecoration = DividerItemDecoration(this, (layoutManager as LinearLayoutManager).orientation)
        taskChecklistView.addItemDecoration(dividerItemDecoration)
        // Setup ItemTouchHelper
        val callback = TaskChecklistDragManageAdaptor(
            adapter as TaskChecklistAdaptor, this,
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(taskChecklistView)

        saveButton.setOnClickListener {
            val taskNameForUpdate = taskName.text.toString()
            val taskSummaryForUpdate = taskSummary.text.toString()
            val taskTypeForUpdate = taskType.text.toString()
            val taskStartDateForUpdate = taskStartDate.text.toString()
            val taskEndDateForUpdate = taskEndDate.text.toString()
            val taskForUpdate = TaskUpdateData(taskID, taskNameForUpdate, taskSummaryForUpdate, taskTypeForUpdate, taskStartDateForUpdate, taskEndDateForUpdate)
            taskListViewModel= ViewModelProvider(this)[TaskListViewModel::class.java]
            taskListViewModel.updateTask(boardID, taskForUpdate)
            finish()
        }

        closeButton.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.custom_task_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.delete_task_btn){
            val confirmationBuilder = AlertDialog.Builder(this)
            val selectedTask = intent.getParcelableExtra<Task>("task")
            confirmationBuilder.setMessage("Are you sure you want to Delete Task <${selectedTask?.name}>?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val taskID = selectedTask?.taskID.toString()
                    val boardID = intent.getSerializableExtra("boardID").toString()
                    taskListViewModel= ViewModelProvider(this)[TaskListViewModel::class.java]
                    taskListViewModel.delete(boardID, taskID)
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = confirmationBuilder.create()
            alert.show()
        }
        return super.onOptionsItemSelected(item)
    }
}