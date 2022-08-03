package com.example.cmpt362project.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayTaskActivity : AppCompatActivity() {

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
            val id = intent.getParcelableExtra<Task>("task")?.taskID
            var boardID = intent.getSerializableExtra("boardID").toString()
            val taskListViewModel: TaskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
            taskListViewModel.delete(boardID, id.toString())
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}