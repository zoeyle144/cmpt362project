package com.example.cmpt362project.models

data class TaskUpdateData(
    val taskID: String = "",
    val name: String = "",
    val summary: String = "",
    val type: String = "",
    val assignedUser: String = "",
    val assignedUserID: String = "",
    val startDate: Long = 0,
    val endDate: Long = 0
)