package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable
import com.example.cmpt362project.database.User
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Group(
    val groupID: String = "",
    val groupName: String = "",
    val description: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    val roles: List<Permission> = ArrayList()
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        arrayListOf<Permission>().also { parcel.readTypedList(it,Permission.CREATOR) },
    //    arrayListOf<Category>().also { parcel.readTypedList(it, Category.CREATOR) },
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(groupID)
        parcel.writeString(groupName)
        parcel.writeString(description)
        parcel.writeString(createdBy)
        parcel.writeStringList(assignedTo)
        parcel.writeTypedList(roles)
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