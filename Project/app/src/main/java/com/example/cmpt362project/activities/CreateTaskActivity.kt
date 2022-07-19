package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Task
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
        createTaskButton.setOnClickListener{

            val database = Firebase.database
            val tasksRef = database.getReference("tasks")
            val taskID = tasksRef.push().key!!
            val taskName = createTaskName.text
            val taskSummary = createTaskSummary.text
            val taskType = createTaskType.text
            val category = intent.getSerializableExtra("category_title")
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val task = Task(taskName.toString(), taskSummary.toString(), taskType.toString(), createdBy.toString(), category.toString())

            tasksRef.child(taskID).setValue(task)
                .addOnCompleteListener{
                    Toast.makeText(this, "Task Created Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{ err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            finish()

        }
    }
}