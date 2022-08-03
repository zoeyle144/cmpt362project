package com.example.cmpt362project.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.*
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
    private var months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        val createTaskButton = findViewById<Button>(R.id.create_task_button)
        val createTaskName = findViewById<EditText>(R.id.create_task_name_input)
        val createTaskSummary = findViewById<EditText>(R.id.create_task_summary_input)
        val createTaskType = findViewById<Spinner>(R.id.create_task_type_input)
        var startDateButton = findViewById<Button>(R.id.start_date_button)
        val startDate = findViewById<TextView>(R.id.start_date_display)
        var endDateButton = findViewById<Button>(R.id.end_date_button)
        val endDate = findViewById<TextView>(R.id.end_date_display)
        ArrayAdapter.createFromResource(
            this,
            R.array.prioLvs,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            createTaskType.adapter = adapter
        }

        startDateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val builder = DatePickerDialog.OnDateSetListener{
                    _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                startDate.text = "$dayOfMonth ${months[month]}, $year"
            }
            val res = DatePickerDialog(
                this,
                builder,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            res.show()
        }

        endDateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val builder = DatePickerDialog.OnDateSetListener{
                    _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                endDate.text = "$dayOfMonth ${months[month]}, $year"
            }
            val res = DatePickerDialog(
                this,
                builder,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            res.show()
        }

        val boardID = intent.getSerializableExtra("boardID").toString()
        createTaskButton.setOnClickListener{
            val database = Firebase.database
            val tasksRef = database.getReference("boards")
            val taskID = tasksRef.child(boardID).child("tasks").push().key!!
            val taskListViewModel: TaskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
            val taskName = createTaskName.text
            val taskSummary = createTaskSummary.text
            val taskType = createTaskType.selectedItem.toString()
            val category = intent.getSerializableExtra("category_title")
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val task = Task(taskID,taskName.toString(), taskSummary.toString(), taskType.toString(), createdBy.toString(), category.toString(), startDate.text.toString(), endDate.text.toString())
            taskListViewModel.insert(task, boardID)
            finish()
        }
    }
}