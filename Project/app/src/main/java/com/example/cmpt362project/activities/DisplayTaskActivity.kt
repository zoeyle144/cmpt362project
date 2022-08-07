package com.example.cmpt362project.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.TaskChecklistAdaptor
import com.example.cmpt362project.adaptors.TaskChecklistDragManageAdaptor
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.models.TaskChecklistItem
import com.example.cmpt362project.models.TaskUpdateData
import com.example.cmpt362project.viewModels.TaskChecklistViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel
import java.util.*
import kotlin.collections.ArrayList
import android.icu.util.Calendar
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

class DisplayTaskActivity : AppCompatActivity() {
    private lateinit var taskListViewModel: TaskListViewModel
    private lateinit var taskChecklistViewModel: TaskChecklistViewModel
    private var months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private var days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private var difficulties = arrayOf("Easy", "Medium", "Hard")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_task)

        val boardID = intent.getSerializableExtra("boardID").toString()

        val t = intent.getParcelableExtra<Task>("task")
        val taskName = findViewById<EditText>(R.id.task_name)
        val taskSummary = findViewById<EditText>(R.id.task_summary)
        val taskType = findViewById<Spinner>(R.id.task_type)
        val taskStartDate = findViewById<EditText>(R.id.task_start_date)
        val taskEndDate = findViewById<EditText>(R.id.task_end_date)

        ArrayAdapter.createFromResource(
            this,
            R.array.difficultyLv,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            taskType.adapter = adapter
            taskType.setSelection(difficulties.indexOf(t?.type))
        }

        var startTimeInMillis = t?.startDate
        var endTimeInMillis = t?.endDate
        var startDateString = ""
        var endDateString = ""

        if (startTimeInMillis != null && startTimeInMillis != 0L){
            val startDateInstance = Calendar.getInstance()
            startDateInstance.timeInMillis = startTimeInMillis

            val startDateDayOfWeek = days[startDateInstance.get(Calendar.DAY_OF_WEEK)-2]
            val startDateMonth = months[startDateInstance.get(Calendar.MONTH)]
            val startDateDayOfMonth = startDateInstance.get(Calendar.DAY_OF_MONTH)
            val startDateYear = startDateInstance.get(Calendar.YEAR)
            val startDateTimeString = "%02d:%02d".format(startDateInstance.get(Calendar.HOUR),startDateInstance.get(Calendar.MINUTE))
            val amPm = if(startDateInstance.get(Calendar.AM_PM) == Calendar.AM){"AM"}else{"PM"}
            startDateString = "$startDateDayOfWeek," +
                    " $startDateMonth" +
                    " $startDateDayOfMonth," +
                    " $startDateYear" +
                    " at $startDateTimeString $amPm"
        }

        if(endTimeInMillis != null && endTimeInMillis != 0L){
            val endDateInstance = Calendar.getInstance()
            endDateInstance.timeInMillis = endTimeInMillis

            val endDateDayOfWeek = days[endDateInstance.get(Calendar.DAY_OF_WEEK)-2]
            val endDateMonth = months[endDateInstance.get(Calendar.MONTH)]
            val endDateDayOfMonth = endDateInstance.get(Calendar.DAY_OF_MONTH)
            val endDateYear = endDateInstance.get(Calendar.YEAR)
            val endDateTimeString = "%02d:%02d".format(endDateInstance.get(Calendar.HOUR),endDateInstance.get(Calendar.MINUTE))
            val amPm = if(endDateInstance.get(Calendar.AM_PM) == Calendar.AM){"AM"}else{"PM"}
            endDateString = "$endDateDayOfWeek," +
                    " $endDateMonth" +
                    " $endDateDayOfMonth," +
                    " $endDateYear" +
                    " at $endDateTimeString $amPm"
        }

        taskStartDate.focusable = View.NOT_FOCUSABLE
        taskEndDate.focusable = View.NOT_FOCUSABLE

        taskName.setText(t?.name)
        taskSummary.setText(t?.summary)
        taskStartDate.setText(startDateString)
        taskEndDate.setText(endDateString)

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
        taskChecklistViewModel.fetchChecklistItems(boardID, taskID)
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

        taskStartDate.setOnClickListener{
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
                taskStartDate.setText(startDateString)
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
                    taskStartDate.setText(startDateString)
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

        taskEndDate.setOnClickListener{
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
                taskEndDate.setText(endDateString)
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
                    taskEndDate.setText(endDateString)
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


        val saveButton = findViewById<Button>(R.id.save_display_task)
        val closeButton = findViewById<Button>(R.id.close_display_task)

        saveButton.setOnClickListener {
            val taskNameForUpdate = taskName.text.toString()
            val taskSummaryForUpdate = taskSummary.text.toString()
            val taskTypeForUpdate = taskType.selectedItem.toString()
            val taskStartDateForUpdate = startTimeInMillis
            val taskEndDateForUpdate = endTimeInMillis
            val taskForUpdate = TaskUpdateData(taskID, taskNameForUpdate, taskSummaryForUpdate, taskTypeForUpdate,
                taskStartDateForUpdate!!,
                taskEndDateForUpdate!!
            )
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