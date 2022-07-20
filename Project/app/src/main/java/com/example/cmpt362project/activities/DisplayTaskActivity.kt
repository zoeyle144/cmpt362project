package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Task

class DisplayTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_task)

        val t = intent.getParcelableExtra<Task>("task")
        val taskName = findViewById<EditText>(R.id.task_name)
        val taskSummary = findViewById<EditText>(R.id.task_summary)
        val taskType = findViewById<EditText>(R.id.task_type)
        val closeButton = findViewById<Button>(R.id.close_display_task)

        taskName.isEnabled = false
        taskSummary.isEnabled = false
        taskType.isEnabled = false

        taskName.setText(t?.name)
        taskSummary.setText(t?.summary)
        taskType.setText(t?.type)

        closeButton.setOnClickListener {
            finish()
        }
    }
}