package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Task(
    val taskID: String = "",
    val name: String = "",
    val summary: String = "",
    val type: String = "",
    val createdBy: String = "",
    val category: String = "",
    val startDate: Long = 0,
    val endDate: Long = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(taskID)
        parcel.writeString(name)
        parcel.writeString(summary)
        parcel.writeString(type)
        parcel.writeString(createdBy)
        parcel.writeString(category)
        parcel.writeLong(startDate)
        parcel.writeLong(endDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "taskID" to taskID,
            "name" to name,
            "summary" to summary,
            "type" to type,
            "createdBy" to createdBy,
            "category" to category,
            "startDate" to startDate,
            "endDate" to endDate
        )
    }
}