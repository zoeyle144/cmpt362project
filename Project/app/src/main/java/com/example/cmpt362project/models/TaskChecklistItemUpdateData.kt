package com.example.cmpt362project.models

data class TaskChecklistItemUpdateData(
    val taskCheckListItemID: String = "",
    val name: String = "",
    val complete: Boolean = false
)