package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable
import com.example.cmpt362project.database.User
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Group(
    val groupName: String = "",
    val description: String = "",
    val createdBy: String = "",
    val members: List<String> = ArrayList(),
    val boards: List<Board> = ArrayList()
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
//        val files = parcel.createStringList(),
        parcel.createStringList(),

        arrayListOf<Board>().also { parcel.readTypedList(it,Board.CREATOR) },
    //    arrayListOf<Category>().also { parcel.readTypedList(it, Category.CREATOR) },
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(groupName)
        parcel.writeString(description)
        parcel.writeString(createdBy)
        parcel.writeStringList(members)
        parcel.writeTypedList(boards)
    //    parcel.writeTypedList(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }
}

fun Parcel.createStringList(): List<String> {
    val size = readString()!!.length
    val output = ArrayList<String>(size)
    for (i in 0 until size) {
        readString()?.let { output.add(it) }
    }
    return output
}

fun Parcel.writeStringList(input:List<String>) {
    writeInt(input.size) // Save number of elements.
    return input.forEach(this::writeString) // Save each element.
}