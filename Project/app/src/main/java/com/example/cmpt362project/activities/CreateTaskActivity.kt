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
import java.sql.Timestamp

class CreateTaskActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private var days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

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
        var startTimeInMillis: Long = 0
        var endTimeInMillis: Long = 0


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
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND,0)
                startTimeInMillis = cal.timeInMillis
                var startDateString = "$dayOfMonth ${months[month]}, $year"
                startDate.text = startDateString
                val builder2 = TimePickerDialog.OnTimeSetListener{
                        _, hour, minute->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    cal.set(Calendar.SECOND, 0)
                    cal.set(Calendar.MILLISECOND,0)
                    val timeString = "%02d:%02d".format(hour,minute)
                    val dayOfWeek = days[cal.get(Calendar.DAY_OF_WEEK)-2]
                    val amPM = if(cal.get(Calendar.AM_PM) == Calendar.AM){"AM"}else{"PM"}
                    startTimeInMillis = cal.timeInMillis
                    startDateString = "${dayOfWeek}, ${months[month]} $dayOfMonth, $year at $timeString $amPM"
                    startDate.text= startDateString
                }
                val res2 = TimePickerDialog(
                    this,
                    builder2,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                )
                res2.show()
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
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND,0)
                endTimeInMillis = cal.timeInMillis
                var endDateString = "$dayOfMonth ${months[month]}, $year"
                endDate.text = endDateString
                val builder2 = TimePickerDialog.OnTimeSetListener{
                        _, hour, minute->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    cal.set(Calendar.SECOND, 0)
                    cal.set(Calendar.MILLISECOND,0)
                    val timeString = "%02d:%02d".format(hour,minute)
                    val dayOfWeek = days[cal.get(Calendar.DAY_OF_WEEK)-2]
                    val amPM = if(cal.get(Calendar.AM_PM) == Calendar.AM){"AM"}else{"PM"}
                    endTimeInMillis = cal.timeInMillis
                    endDateString = "${dayOfWeek}, ${months[month]} $dayOfMonth, $year at $timeString $amPM"
                    endDate.text= endDateString
                }
                val res2 = TimePickerDialog(
                    this,
                    builder2,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                )
                res2.show()
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
            val task = Task(taskID,taskName.toString(), taskSummary.toString(), taskType, createdBy.toString(), category.toString(), startTimeInMillis, endTimeInMillis)
            taskListViewModel.insert(task, boardID)
            finish()
        }
    }
}