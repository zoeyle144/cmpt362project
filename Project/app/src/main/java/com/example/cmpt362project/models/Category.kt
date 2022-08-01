package com.example.cmpt362project.models

import com.google.firebase.database.IgnoreExtraProperties
import android.os.Parcel
import android.os.Parcelable

@IgnoreExtraProperties
data class Category(
    val categoryID: String = "",
    val title: String = "",
    val createdBy: String = "",
    val board: String = "",
//    val tasks: List<Task> = ArrayList()
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
//        arrayListOf<Task>().also { parcel.readTypedList(it, Task.CREATOR) },
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(categoryID)
        parcel.writeString(title)
        parcel.writeString(createdBy)
        parcel.writeString(board)
//        parcel.writeTypedList(tasks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}
