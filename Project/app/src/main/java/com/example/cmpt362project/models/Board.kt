package com.example.cmpt362project.models

import com.google.firebase.database.IgnoreExtraProperties
import android.os.Parcel
import android.os.Parcelable

@IgnoreExtraProperties
data class Board(
    val boardID: String = "",
    val boardName: String = "",
    val description: String = "",
    val createdBy: String = "",
    val boardPic: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(boardID)
        parcel.writeString(boardName)
        parcel.writeString(description)
        parcel.writeString(createdBy)
        parcel.writeString(boardPic)
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
