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
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.models.TaskUpdateData
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayTaskActivity : AppCompatActivity() {
    private lateinit var taskListViewModel: TaskListViewModel

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

        saveButton.setOnClickListener {
            val taskNameForUpdate = findViewById<EditText>(R.id.task_name).text.toString()
            val taskSummaryForUpdate = findViewById<EditText>(R.id.task_summary).text.toString()
            val taskTypeForUpdate = findViewById<EditText>(R.id.task_type).text.toString()
            val taskStartDateForUpdate = findViewById<EditText>(R.id.task_start_date).text.toString()
            val taskEndDateForUpdate = findViewById<EditText>(R.id.task_end_date).text.toString()
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