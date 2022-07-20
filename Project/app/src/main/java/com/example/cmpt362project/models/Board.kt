package com.example.cmpt362project.models

import com.google.firebase.database.IgnoreExtraProperties
import android.os.Parcel
import android.os.Parcelable

@IgnoreExtraProperties
data class Board(
    val boardName: String = "",
    val description: String = "",
    val createdBy: String = "",
    val categories: List<Category> = ArrayList()
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        arrayListOf<Category>().also { parcel.readTypedList(it, Category.CREATOR) },
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(boardName)
        parcel.writeString(description)
        parcel.writeString(createdBy)
        parcel.writeTypedList(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Board> {
        override fun createFromParcel(parcel: Parcel): Board {
            return Board(parcel)
        }

        override fun newArray(size: Int): Array<Board?> {
            return arrayOfNulls(size)
        }
    }
}
