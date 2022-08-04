package com.example.cmpt362project.models

data class TaskUpdateData(
    val taskID: String = "",
    val name: String = "",
    val summary: String = "",
    val type: String = "",
    val startDate: String = "",
    val endDate: String = ""
)