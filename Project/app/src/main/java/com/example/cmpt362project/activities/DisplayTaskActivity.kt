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
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayTaskActivity : AppCompatActivity() {
    private lateinit var taskListViewModel: TaskListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_task)

        val t = intent.getParcelableExtra<Task>("task")
        val taskName = findViewById<EditText>(R.id.task_name)
        val taskSummary = findViewById<EditText>(R.id.task_summary)
        val taskType = findViewById<EditText>(R.id.task_type)
        val taskStartDate = findViewById<EditText>(R.id.task_start_date)
        val taskEndDate = findViewById<EditText>(R.id.task_end_date)
        val closeButton = findViewById<Button>(R.id.close_display_task)

        taskName.isEnabled = false
        taskSummary.isEnabled = false
        taskType.isEnabled = false
        taskStartDate.isEnabled = false
        taskEndDate.isEnabled = false

        taskName.setText(t?.name)
        taskSummary.setText(t?.summary)
        taskType.setText(t?.type)
        taskStartDate.setText(t?.startDate)
        taskEndDate.setText(t?.endDate)

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