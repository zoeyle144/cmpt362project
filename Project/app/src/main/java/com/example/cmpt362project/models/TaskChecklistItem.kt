package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TaskChecklistItem(
    val taskChecklistItemID: String = "",
    val name: String = "",
    val createdBy: String = "",
    val complete: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(taskChecklistItemID)
        parcel.writeString(name)
        parcel.writeString(createdBy)
        parcel.writeBoolean(complete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskChecklistItem> {
        override fun createFromParcel(parcel: Parcel): TaskChecklistItem {
            return TaskChecklistItem(parcel)
        }

        override fun newArray(size: Int): Array<TaskChecklistItem?> {
            return arrayOfNulls(size)
        }
    }

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "taskChecklistItemID" to taskChecklistItemID,
            "name" to name,
            "createdBy" to createdBy,
            "complete" to complete
        )
    }
}
