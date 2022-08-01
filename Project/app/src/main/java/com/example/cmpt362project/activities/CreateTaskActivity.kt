package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateTaskActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        val createTaskButton = findViewById<Button>(R.id.create_task_button)
        val createTaskName = findViewById<EditText>(R.id.create_task_name_input)
        val createTaskSummary = findViewById<EditText>(R.id.create_task_summary_input)
        val createTaskType = findViewById<EditText>(R.id.create_task_type_input)
        val boardID = intent.getSerializableExtra("boardID").toString()
        createTaskButton.setOnClickListener{
            val database = Firebase.database
            val tasksRef = database.getReference("boards")
            val taskID = tasksRef.child(boardID).child("tasks").push().key!!
            val taskListViewModel: TaskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
            val taskName = createTaskName.text
            val taskSummary = createTaskSummary.text
            val taskType = createTaskType.text
            val category = intent.getSerializableExtra("category_title")
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val task = Task(taskID,taskName.toString(), taskSummary.toString(), taskType.toString(), createdBy.toString(), category.toString())
            taskListViewModel.insert(task, boardID)
            finish()

        }
    }
}