package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Permission(
    val permissionID: String = "",
    val role: String = "",
    val uid: String = "",
    val groupID: String = "",
    val userName: String = "",

    ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(permissionID)
        parcel.writeString(role)
        parcel.writeString(uid)
        parcel.writeString(groupID)
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Permission> {
        override fun createFromParcel(parcel: Parcel): Permission {
            return Permission(parcel)
        }

        override fun newArray(size: Int): Array<Permission?> {
            return arrayOfNulls(size)
        }
    }
}
