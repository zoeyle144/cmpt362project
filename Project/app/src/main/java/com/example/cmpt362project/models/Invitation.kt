package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable

data class Invitation (
    var invitationId: String = "Default",
    var sender: String = "Default",
    var sender_username: String = "Default",
    var receiver: String = "Default",
    var groupId: String = "Default") :Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(invitationId)
        parcel.writeString(sender)
        parcel.writeString(sender_username)
        parcel.writeString(receiver)
        parcel.writeString(groupId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Invitation> {
        override fun createFromParcel(parcel: Parcel): Invitation {
            return Invitation(parcel)
        }

        override fun newArray(size: Int): Array<Invitation?> {
            return arrayOfNulls(size)
        }
    }

}